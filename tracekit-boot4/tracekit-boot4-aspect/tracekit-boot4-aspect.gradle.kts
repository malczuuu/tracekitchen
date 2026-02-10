plugins {
    id("internal.java-library-convention")
    id("internal.publishing-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekit-boot4:tracekit-boot4-core"))

    annotationProcessor(platform(libs.spring.boot.dependencies))
    annotationProcessor(libs.spring.boot.configuration.processor)

    compileOnly(libs.spring.boot.starter.aspectj)

    testImplementation(libs.spring.boot.starter.aspectj.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

// see buildSrc/src/main/kotlin/internal.publishing-convention.gradle.kts
internalPublishing {
    displayName = "TraceKit Boot4 Aspect"
    description = "TraceKit Project - Aspect Integration Module - Spring Boot 4"
}
