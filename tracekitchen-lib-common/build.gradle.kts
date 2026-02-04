plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen-lib-bom")))

    api(libs.jackson.databind)
    api(libs.problem4j.core)

    compileOnly(libs.spring.boot.micrometer.tracing)
    compileOnly(libs.spring.boot.starter.webmvc)
}
