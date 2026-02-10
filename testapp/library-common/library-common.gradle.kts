plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":testapp:library-bom")))

    api(project(":tracekit-spring:tracekit-spring-starter"))

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
