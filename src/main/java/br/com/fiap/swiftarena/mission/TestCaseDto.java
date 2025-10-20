package br.com.fiap.swiftarena.mission;

public record TestCaseDto(
        String input,
        String expectedOutput
) {
    public static TestCaseDto from(TestCase testCase) {
        return new TestCaseDto(
                testCase.getInput(),
                testCase.getExpectedOutput()
        );
    }
}

