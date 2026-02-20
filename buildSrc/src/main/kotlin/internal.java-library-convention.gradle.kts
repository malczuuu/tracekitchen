plugins {
    id("internal.java-convention")
    id("java-library")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.named<JavaCompile>("compileJava") {
    options.release = 17
}

tasks.withType<Javadoc>().configureEach {
    javadocTool = javaToolchains.javadocToolFor { languageVersion = JavaLanguageVersion.of(17) }
}
