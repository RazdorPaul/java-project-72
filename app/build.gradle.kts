plugins {
    id("java")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.sonarqube") version "7.3.1.8318"
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

    // AssertJ для удобных ассертов
    testImplementation("org.assertj:assertj-core:3.24.2")
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
                minimum = "0.70".toBigDecimal() // 70% покрытие
            }
        }
    }
}

// Добавляем проверку JaCoCo в task check
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}