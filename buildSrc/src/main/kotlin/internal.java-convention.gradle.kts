import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("internal.common-convention")
    id("java")
}

java {
    toolchain.languageVersion = providers.gradleProperty("internal.java.version").map { JavaLanguageVersion.of(it) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes["Implementation-Title"] = project.name
        attributes["Build-Jdk-Spec"] = java.toolchain.languageVersion.get().toString()
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"

        if (project.version != Project.DEFAULT_VERSION) {
            attributes["Implementation-Version"] = project.version
        }
    }
    from("${rootProject.rootDir}/LICENSE") {
        into("META-INF/")
        rename { "LICENSE.txt" }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        exceptionFormat = TestExceptionFormat.SHORT
        showStandardStreams = true
    }

    // For resolving warnings from mockito.
    jvmArgs("-XX:+EnableDynamicAgentLoading")

    systemProperty("user.language", "en")
    systemProperty("user.country", "US")
}
