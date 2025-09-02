package br.com.fiap.swiftarena.mission;

import br.com.fiap.swiftarena.lesson.Lesson;

public record MissionStep(
        Long id,
        String title,
        boolean challenge,
        Lesson lesson,
        boolean completed,
        boolean haveSubmission
) {
}
