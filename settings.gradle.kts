pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "tracekitchen"

include(":tracekitchen-app-downstream")
include(":tracekitchen-app-entrypoint")
include(":tracekitchen-lib-bom")
include(":tracekitchen-lib-common")
