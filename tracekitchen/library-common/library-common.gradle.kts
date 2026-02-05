plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))

    api(libs.spring.boot.starter.restclient)
    api(libs.spring.boot.starter.webmvc)

    api(libs.jackson.databind)
    api(libs.problem4j.spring.webmvc)
    api(libs.springdoc.openapi.starter.webmvc.ui)
}
