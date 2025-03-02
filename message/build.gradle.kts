plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":core"))
    
    // Spring
    implementation("org.springframework.boot:spring-boot-starter:3.2.0")
    
    // Spring Cloud Stream
    implementation("org.springframework.cloud:spring-cloud-stream:4.0.4")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:4.0.4")
    
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
    
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support:4.0.4")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.testcontainers:kafka:1.19.3")
}

tasks.register("make") {
    group = "build"
    description = "Run all verification tasks"
    dependsOn(
        "ktlintCheck",
        "detekt",
        "test",
        "jacocoTestReport",
        "jacocoTestCoverageVerification",
        "dependencyCheckAnalyze"
    )
}