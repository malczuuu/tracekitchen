plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":tracekitchen:library-bom")))
    api(project(":tracekitchen:library-tracing-spring"))

    compileOnly(libs.spring.boot.starter.aspectj)

    testImplementation(libs.spring.boot.starter.aspectj.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
