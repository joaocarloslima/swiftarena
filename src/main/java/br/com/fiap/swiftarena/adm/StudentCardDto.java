package br.com.fiap.swiftarena.adm;

import lombok.Data;

import java.util.List;

@Data
public class StudentCardDto {

    private Long id;
    private String name;
    private String avatarUrl;
    private int totalScore;

    private List<MissionCardDto> missions;

}
