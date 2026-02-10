plugins {
    id("internal.java-library-convention")
}

dependencies {
    compileOnly(platform(project(":testapp:library-bom")))

    api(project(":tracekit-boot4:tracekit-boot4-starter"))

    compileOnly(libs.spring.boot.starter.aspectj)
    compileOnly(libs.spring.boot.starter.restclient)
    compileOnly(libs.spring.boot.starter.webmvc)

    api(libs.jackson.databind)
    api(libs.problem4j.spring.webmvc)
    api(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(platform(project(":testapp:library-bom")))
    testImplementation(libs.spring.boot.starter.aspectj.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.restclient.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
