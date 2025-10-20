package br.com.fiap.swiftarena.mission;


import java.util.List;

public record MissionRequest(
        Long id,
        String title,
        boolean challenge,
        Long lessonId,
        String descriptionMarkdown,
        List<TestCase> testCases
) {
}
