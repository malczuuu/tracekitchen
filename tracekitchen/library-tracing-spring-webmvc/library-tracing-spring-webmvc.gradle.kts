plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))
    api(project(":tracekitchen:library-tracing"))

    api(libs.spring.boot.starter.restclient)
    api(libs.spring.boot.starter.webmvc)

    api(libs.jackson.databind)
    api(libs.problem4j.spring.webmvc)
    api(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
