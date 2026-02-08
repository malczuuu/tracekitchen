plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":internal:library-bom")))
    api(project(":tracekitchen-spring"))

    compileOnly(libs.spring.boot.starter.webmvc)

    testImplementation(libs.spring.boot.starter.webmvc.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
