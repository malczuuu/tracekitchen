plugins {
    id("internal.java-library-convention")
    id("internal.publishing-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekit"))
    api(project(":tracekit-boot4:tracekit-boot4-core"))
    api(project(":tracekit-boot4:tracekit-boot4-aspect"))
    api(project(":tracekit-boot4:tracekit-boot4-restclient"))
    api(project(":tracekit-boot4:tracekit-boot4-webmvc"))
}

// see buildSrc/src/main/kotlin/internal.publishing-convention.gradle.kts
internalPublishing {
    displayName = "TraceKit Boot4 Starter"
    description = "TraceKit Project - Starter Module - Spring Boot 4"
}
