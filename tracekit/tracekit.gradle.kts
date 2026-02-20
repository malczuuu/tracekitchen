plugins {
    id("internal.errorprone-convention")
    id("internal.java-library-convention")
    id("internal.publishing-convention")
    alias(libs.plugins.nmcp)
}

dependencies {
    compileOnly(platform(libs.spring.boot.dependencies))

    api(libs.jspecify)

    testImplementation(platform(libs.spring.boot.dependencies))
    testImplementation(libs.assertj.core)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.testclock)
    testRuntimeOnly(libs.junit.platform.launcher)

    errorprone(libs.errorprone.core)
    errorprone(libs.nullaway)
}

// see buildSrc/src/main/kotlin/internal.publishing-convention.gradle.kts
internalPublishing {
    displayName = "TraceKit"
    description = "TraceKit Project - Core Module"
}

tasks.named<JavaCompile>("compileJava") {
    options.release = 8
}

tasks.withType<Javadoc>().configureEach {
    javadocTool = javaToolchains.javadocToolFor { languageVersion = JavaLanguageVersion.of(8) }
}
