plugins {
    id("internal.java-convention")
    id("java-library")
}

java {
    withSourcesJar()
    withJavadocJar()
}
