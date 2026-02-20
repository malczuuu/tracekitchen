import com.diffplug.spotless.LineEnding
import internal.getBooleanProperty

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
    val licenseHeader = "${rootProject.rootDir}/gradle/license-header.java"
    val updateLicenseYear = project.getBooleanProperty("spotless.license-year-enabled")

    java {
        target("tracekit/src/**/*.java", "tracekit-boot4/*/src/**/*.java")
        licenseHeaderFile(licenseHeader).updateYearWithLatest(updateLicenseYear)

        // NOTE: decided not to upgrade Google Java Format, as versions 1.29+ require running it on Java 21
        googleJavaFormat("1.28.0")
        forbidWildcardImports()
        endWithNewline()
        lineEndings = LineEnding.UNIX
    }
    format("javaMisc") {
        target(
            "tracekit/src/**/package-info.java",
            "tracekit-boot4/*/src/**/package-info.java",
        )

        // License headers in these files are not formatted with standard java group, so we need to use custom settings.
        // The regex is designed to find out where the code starts in these files, so the license header can be placed
        // before it.
        //
        // The code starts with either:
        //
        // - any annotation (ex. @NullMarked before package declaration),
        // - package, module or import declaration,
        // - "/**" in case of a pre-package (or pre-module) JavaDoc.
        val delimiter = "^(@|package|import|module|/\\*\\*)"

        licenseHeaderFile(licenseHeader, delimiter).updateYearWithLatest(updateLicenseYear)
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

defaultTasks("spotlessApply", "build")
