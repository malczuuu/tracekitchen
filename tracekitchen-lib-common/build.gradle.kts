plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen-lib-bom")))

    compileOnly(libs.spring.boot.micrometer.tracing)
    compileOnly(libs.spring.boot.starter.restclient)
    compileOnly(libs.spring.boot.starter.webmvc)

    compileOnly(libs.jackson.databind)
    compileOnly(libs.problem4j.spring.webmvc)
}
