plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekit-spring:tracekit-spring-core"))

    annotationProcessor(platform(libs.spring.boot.dependencies))
    annotationProcessor(libs.spring.boot.configuration.processor)

    compileOnly(libs.spring.boot.starter.aspectj)

    testImplementation(libs.spring.boot.starter.aspectj.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
