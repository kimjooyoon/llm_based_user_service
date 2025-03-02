import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    kotlin("jvm") version "1.9.20" apply false
    kotlin("plugin.spring") version "1.9.20" apply false
    kotlin("plugin.jpa") version "1.9.20" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.4" apply false
    id("org.owasp.dependencycheck") version "8.4.0" apply false
    id("org.sonarqube") version "4.4.1.3373" apply false
    id("jacoco")
}

allprojects {
    group = "com.github.kimjooyoon"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.owasp.dependencycheck")
        plugin("jacoco")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
        dependsOn(tasks.test)
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.80".toBigDecimal()
                }
            }
        }
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
        testImplementation("org.assertj:assertj-core:3.24.2")
    }
}

tasks.register("clean") {
    doFirst {
        delete(rootProject.buildDir)
    }
}

tasks.register("make") {
    group = "build"
    description = "Run all verification tasks"
    dependsOn(
        ":ktlintCheck",
        ":detekt",
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification",
        ":dependencyCheckAnalyze"
    )
}

tasks.register("makeAll") {
    group = "build"
    description = "Run make for all subprojects"
    subprojects.forEach { subproject ->
        dependsOn(subproject.tasks.matching { it.name == "make" })
    }
}