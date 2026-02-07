plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))
    api(project(":tracekitchen:library-tracing"))

    api(libs.spring.boot.starter)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
