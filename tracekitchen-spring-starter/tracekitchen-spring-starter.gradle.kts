plugins {
    id("internal.java-library-convention")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":tracekitchen-core"))
    api(project(":tracekitchen-spring"))
    api(project(":tracekitchen-spring-aspect"))
    api(project(":tracekitchen-spring-restclient"))
    api(project(":tracekitchen-spring-webmvc"))
}
