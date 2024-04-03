## Requirements
- Java 21 (use [sdkman](https://sdkman.io/))
- Docker or [TCD](https://testcontainers.com/desktop/)
- OpenAI API key defined in `src/main/resources/application.properties` or provided as an environment variable `OPENAI_API_KEY`

## Description
This is a simple web application that shows how to test LLM models responses

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