## Requirements
- Java 21 (use [sdkman](https://sdkman.io/))
- [TCC](https://testcontainers.com/desktop/) and GPU enabled zone or run Ollama locally
- OpenAI API key `openai.api-key` defined in `src/main/resources/application.properties` or provided as an environment variable `OPENAI_API_KEY`

## Description
This project serves as an example of how to test GenAI applications by using a Large Language Model (LLM).

The main challenge with verifying answers from LLMs is that they generate responses in natural language that are non-deterministic, making traditional testing methods, which rely on predictable outcomes, unsuitable. 
To address this, the proposed solution involves using one LLM to assess the adequacy of another LLM's responses. This involves setting detailed validation criteria and employing an LLM as a 'Validator Agent' to ensure the responses meet these criteria. This method can be used to validate answers that require both general and specialized knowledge:

```
    String question = "Does 'good' have the same meaning as 'bad'?";
    String reference = "good is the opposite of bad";

    @Test
    void verifyValidatorDetectsWrongAnswer() {
        String answer = "Yes";
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        assertThat(validate.response()).isEqualTo("no");
    }

    @Test
    void verifyValidatorDetectsGoodAnswer() {
        String answer = "No";
        ValidatorAgent.ValidatorResponse validate = validatorAgent.validate(question, answer, reference);
        assertThat(validate.response()).isEqualTo("yes");
    }
```
The `ValidatorAgent` is an [AI Service](https://docs.langchain4j.dev/tutorials/ai-services/) responsible for validating the answers. It will verify if the answer is correct or not based on the reference provided.

## How to run tests
```shell
./gradlew test
```

## How to run backend
```shell
./gradlew run
```

## How to run frontend
```shell
cd frontend
npm install
npm start
```