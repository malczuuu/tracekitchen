plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekit"))
    api(project(":tracekit-boot4:tracekit-boot4-core"))
    api(project(":tracekit-boot4:tracekit-boot4-aspect"))
    api(project(":tracekit-boot4:tracekit-boot4-restclient"))
    api(project(":tracekit-boot4:tracekit-boot4-webmvc"))
}
