plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    api(project(":tracekitchen-spring:tracekitchen-spring-core"))

    annotationProcessor(platform(libs.spring.boot.dependencies))
    annotationProcessor(libs.spring.boot.configuration.processor)

    compileOnly(libs.spring.boot.starter.webmvc)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
