plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))

    api(project(":tracekitchen:library-tracing-spring-restclient"))
    api(project(":tracekitchen:library-tracing-spring-webmvc"))

    compileOnly(libs.spring.boot.starter.restclient)
    compileOnly(libs.spring.boot.starter.webmvc)

    api(libs.jackson.databind)
    api(libs.problem4j.spring.webmvc)
    api(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.restclient.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
