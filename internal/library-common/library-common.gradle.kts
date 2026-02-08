plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":internal:library-bom")))

    api(project(":tracekitchen-spring-aspect"))
    api(project(":tracekitchen-spring-restclient"))
    api(project(":tracekitchen-spring-webmvc"))

    compileOnly(libs.spring.boot.starter.aspectj)
    compileOnly(libs.spring.boot.starter.restclient)
    compileOnly(libs.spring.boot.starter.webmvc)

    api(libs.jackson.databind)
    api(libs.problem4j.spring.webmvc)
    api(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.aspectj.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.restclient.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
