import com.diffplug.spotless.LineEnding

plugins {
    id("internal.common-convention")
    id("internal.idea-convention")
    alias(libs.plugins.spotless)
    alias(libs.plugins.nmcp).apply(false)
    alias(libs.plugins.nmcp.aggregation)
}

dependencies {
    nmcpAggregation(project(":tracekit"))
    nmcpAggregation(project(":tracekit-boot4:tracekit-boot4-aspect"))
    nmcpAggregation(project(":tracekit-boot4:tracekit-boot4-core"))
    nmcpAggregation(project(":tracekit-boot4:tracekit-boot4-restclient"))
    nmcpAggregation(project(":tracekit-boot4:tracekit-boot4-starter"))
    nmcpAggregation(project(":tracekit-boot4:tracekit-boot4-webmvc"))
}

nmcpAggregation {
    centralPortal {
        username = System.getenv("PUBLISHING_USERNAME")
        password = System.getenv("PUBLISHING_PASSWORD")

        publishingType = "USER_MANAGED"
    }
}

spotless {
    java {
        target("**/src/**/*.java")

        // NOTE: decided not to upgrade Google Java Format, as versions 1.29+ require running it on Java 21
        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
    kotlin {
        target("**/src/**/*.kt")

        ktfmt("0.60").metaStyle()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")

        ktlint("1.8.0").editorConfigOverride(mapOf("max_line_length" to "120"))
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
    format("yaml") {
        target("**/*.yml", "**/*.yaml")
        targetExclude("**/build/**")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
    format("misc") {
        target("**/.gitattributes", "**/.gitignore")

        trimTrailingWhitespace()
        leadingTabsToSpaces(4)
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
}
