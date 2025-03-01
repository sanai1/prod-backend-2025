FROM huecker.io/library/gradle:8.7.0-jdk as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .

RUN gradle copyDependencies -x test --no-daemon

COPY src src

RUN gradle build -x test --no-daemon --stacktrace

FROM huecker.io/library/amazoncorretto:21

WORKDIR /app

COPY --from=build /workspace/app/build/libs/example-2025-0.0.1.jar ./app.jar
COPY --from=build /workspace/app/build/libs/libraries ./libraries

CMD ["java", "-cp", "libraries/*:app.jar", "ru.kotleteri.ApplicationKt"]