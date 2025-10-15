package br.com.fiap.swiftarena.mission;


import java.util.List;

public record MissionRequest(
        String title,
        boolean challenge,
        Long lessonId,
        String descriptionMarkdown,
        List<TestCase> testCases
) {
}
