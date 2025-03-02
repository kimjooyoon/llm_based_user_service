plugins {
    kotlin("jvm")
}

dependencies {
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.3")
    
    // Test
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
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