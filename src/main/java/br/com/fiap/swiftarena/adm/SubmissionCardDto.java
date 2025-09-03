package br.com.fiap.swiftarena.adm;

import lombok.Data;

@Data
public class SubmissionCardDto {

    private boolean passed;
    private int attempts;
    private int opacity;
    private String className;

}
