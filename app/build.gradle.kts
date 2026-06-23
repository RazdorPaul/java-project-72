plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.sonarqube") version "7.3.1.8318"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
    checkstyle
    jacoco
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.7")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("io.javalin:javalin:6.7.0")
    testImplementation("io.javalin:javalin-testtools:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("gg.jte:jte:3.2.2")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.github.cdimascio:dotenv-java:3.2.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("com.konghq:unirest-java:3.14.0")
    implementation("org.jsoup:jsoup:1.17.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// Настройка приложения
application {
    mainClass.set("hexlet.code.App")
}

// Checkstyle
checkstyle {
    toolVersion = "10.12.5"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}

// SonarQube
sonar {
    properties {
        property ("sonar.projectKey", "RazdorPaul_java-project-72")
        property ("sonar.organization", "razdorpau")
        property ("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}

// JaCoCo
jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)   // Для SonarQube
        html.required.set(true)  // Для просмотра
        csv.required.set(false)
    }
    // Исключаем main класс из покрытия
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("**/App.class")
        }
    )
}

// Проверка минимального покрытия
tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.85".toBigDecimal() // 85% покрытие
            }
        }
    }
}

// Добавляем проверку JaCoCo в task check
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}