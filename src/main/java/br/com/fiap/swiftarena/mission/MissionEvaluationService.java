package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.submission.Submission;
import br.com.fiap.swiftarena.submission.SubmissionRepository;
import br.com.fiap.swiftarena.user.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionEvaluationService {
    private final int OUTPUT_MAX_LENGTH = 255;

    private static final String CONTAINER_PATH = "/app";
    private final TestCaseRepository testCaseRepository;
    private final SubmissionRepository submissionRepository;

    public MissionEvaluationService(TestCaseRepository testCaseRepository, SubmissionRepository submissionRepository) {
        this.testCaseRepository = testCaseRepository;
        this.submissionRepository = submissionRepository;
    }

    public EvaluationResult evaluate(String code, User user, Mission mission, int attempt) {
        try {
            Path submissionPath = Path.of("submissions", user.getId().toString(), mission.getId().toString(), String.valueOf(attempt));
            Files.createDirectories(submissionPath);

            Path swiftFile = submissionPath.resolve("Main.swift");
            Files.writeString(swiftFile, code);

            // 1. Compilar
            EvaluationResult compilation = compile(submissionPath);
            if (!compilation.success()) {
                saveSubmission(code, user, mission, attempt, compilation);
                return compilation;
            }

            // 2. Rodar testes
            var resultTests = runTests(submissionPath, mission);

            saveSubmission(code, user, mission, attempt, resultTests);

            return resultTests;

        } catch (IOException | InterruptedException e) {
            return new EvaluationResult(false, "⚠️ Erro interno ao tentar executar o código.");
        }
    }

    private void saveSubmission(String code, User user, Mission mission, int attempt, EvaluationResult result) {
        var output = StringUtils.truncate(result.output(), OUTPUT_MAX_LENGTH);
        var submission = Submission.builder()
                    .user(user)
                    .mission(mission)
                    .attempt(attempt)
                    .passed(result.success())
                    .output(output)
                    .code(code)
                    .submittedAt(LocalDateTime.now())
                    .build();
        submissionRepository.save(submission);
    }

    private EvaluationResult compile(Path submissionPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", submissionPath.toAbsolutePath() + ":" + CONTAINER_PATH,
                "-w", CONTAINER_PATH,
                "swift",
                "swiftc", "Main.swift", "-o", "Main"
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();

        String output = readOutput(process);
        int exit = process.waitFor();

        if (exit != 0) {
            return new EvaluationResult(false, "🛑 Erro de compilação:\n\n" + output);
        }
        return new EvaluationResult(true, "");
    }

    private EvaluationResult runTests(Path submissionPath, Mission mission) throws IOException, InterruptedException {
        List<TestCase> tests = testCaseRepository.findByMission(mission);

        for (int i = 0; i < tests.size(); i++) {
            TestCase test = tests.get(i);

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "run", "--rm", "-i",
                    "-v", submissionPath.toAbsolutePath() + ":" + CONTAINER_PATH,
                    "-w", CONTAINER_PATH,
                    "swift",
                    "./Main"
            );
            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (OutputStream stdin = process.getOutputStream()) {
                stdin.write(test.getInput().replace("\\n", "\n").getBytes());
                stdin.flush();
            }

            String output = readOutput(process).trim();
            int exit = process.waitFor();

            if (exit != 0 || !output.equals(test.getExpectedOutput())) {
                return new EvaluationResult(false,
                        "❌ O programa falhou no teste " + (i + 1) + ":\n\n" +
                                "📥 Entrada:\n" + test.getInput().replace("\\n", "\n") + "\n\n" +
                                "✅ Saída esperada:\n" + test.getExpectedOutput() + "\n\n" +
                                "🚫 Saída do seu programa:\n" + output
                );
            }
        }

        return new EvaluationResult(true, "✅ Seu programa passou em todos os testes!");
    }

    private String readOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}