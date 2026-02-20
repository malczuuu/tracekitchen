plugins {
    id("internal.errorprone-convention")
    id("internal.java-library-convention")
    id("internal.publishing-convention")
    alias(libs.plugins.nmcp)
}

dependencies {
    compileOnly(platform(libs.spring.boot.dependencies))

    api(project(":tracekit"))
    api(project(":tracekit-boot4:tracekit-boot4-core"))
    api(project(":tracekit-boot4:tracekit-boot4-aspect"))
    api(project(":tracekit-boot4:tracekit-boot4-restclient"))
    api(project(":tracekit-boot4:tracekit-boot4-webmvc"))

    errorprone(libs.errorprone.core)
    errorprone(libs.nullaway)
}

// see buildSrc/src/main/kotlin/internal.publishing-convention.gradle.kts
internalPublishing {
    displayName = "TraceKit Boot4 Starter"
    description = "TraceKit Project - Starter Module - Spring Boot 4"
}
