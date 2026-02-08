plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":internal:library-bom")))

    api(libs.jspecify)

    testImplementation(libs.assertj.core)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.testclock)
    testRuntimeOnly(libs.junit.platform.launcher)
}
