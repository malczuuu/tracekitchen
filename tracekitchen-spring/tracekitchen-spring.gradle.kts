plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":internal:library-bom")))
    api(project(":tracekitchen-core"))

    compileOnly(libs.spring.boot.starter)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
