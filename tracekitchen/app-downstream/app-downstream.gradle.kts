plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project(":tracekitchen:library-bom")))

    implementation(project(":tracekitchen:library-common"))
    implementation(project(":tracekitchen:library-tracing-spring-webmvc"))

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.restclient)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.problem4j.spring.webmvc)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
