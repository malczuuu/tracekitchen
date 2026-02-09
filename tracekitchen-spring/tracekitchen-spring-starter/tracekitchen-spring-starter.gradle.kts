plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekitchen-core"))
    api(project(":tracekitchen-spring:tracekitchen-spring-core"))
    api(project(":tracekitchen-spring:tracekitchen-spring-aspect"))
    api(project(":tracekitchen-spring:tracekitchen-spring-restclient"))
    api(project(":tracekitchen-spring:tracekitchen-spring-webmvc"))
}
