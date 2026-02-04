plugins {
    id("internal.java-platform-convention")
}

dependencies {
    api(platform(libs.spring.boot.dependencies))
    api(platform(libs.problem4j.spring.bom))
    api(platform(libs.springdoc.openapi.bom))
}
