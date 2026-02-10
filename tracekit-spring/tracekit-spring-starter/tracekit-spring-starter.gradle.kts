plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekit"))
    api(project(":tracekit-spring:tracekit-spring-core"))
    api(project(":tracekit-spring:tracekit-spring-aspect"))
    api(project(":tracekit-spring:tracekit-spring-restclient"))
    api(project(":tracekit-spring:tracekit-spring-webmvc"))
}
