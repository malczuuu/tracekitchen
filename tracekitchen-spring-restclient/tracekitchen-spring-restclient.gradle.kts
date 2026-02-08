plugins {
    id("internal.java-library-convention")
}

dependencies {
    api(platform(project(":internal:library-bom")))
    api(project(":tracekitchen-spring"))

    compileOnly(libs.spring.boot.starter.restclient)

    testImplementation(libs.spring.boot.starter.restclient.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
