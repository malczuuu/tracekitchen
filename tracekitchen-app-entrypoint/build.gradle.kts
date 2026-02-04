plugins {
    id("internal.spring-app-convention")
}

dependencies {
    implementation(platform(project("::tracekitchen-lib-bom")))

    implementation(project(":tracekitchen-lib-common"))

    implementation(libs.spring.boot.micrometer.tracing.opentelemetry)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.restclient)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webmvc)

    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(libs.problem4j.spring.webmvc)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.micrometer.tracing.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
