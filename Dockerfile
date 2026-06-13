FROM eclipse-temurin:21-jdk

WORKDIR /app

# Копируем файлы Gradle из папки app для кэширования зависимостей
COPY app/gradle ./gradle
COPY app/build.gradle.kts .
COPY app/settings.gradle.kts .
COPY app/gradlew .

RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon dependencies

COPY app/src ./src
COPY app/config ./config

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=60.0 -XX:InitialRAMPercentage=50.0"

ENV PORT=7070
EXPOSE 7070

CMD ["java", "-jar", "build/libs/app-1.0-SNAPSHOT-all.jar"]
