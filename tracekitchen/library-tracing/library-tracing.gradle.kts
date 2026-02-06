plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))

    api(libs.jspecify)

    testImplementation(libs.assertj.core)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}
