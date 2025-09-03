package br.com.fiap.swiftarena.adm;

import br.com.fiap.swiftarena.lesson.Lesson;
import lombok.Data;

import java.util.List;

@Data
public class MissionCardDto {

    private Lesson lesson;
    private List<SubmissionCardDto> submissions;

}
