import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings

plugins {
    id("org.jetbrains.gradle.plugin.idea-ext")
}

idea {
    project {
        settings {
            runConfigurations {
                create<Application>("launch app-downstream") {
                    mainClass = "io.github.malczuuu.tracekit.app.downstream.DownstreamApplication"
                    moduleName = "tracekitchen.testapp.app-downstream.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Application>("launch app-entrypoint") {
                    mainClass = "io.github.malczuuu.tracekit.app.entrypoint.EntrypointApplication"
                    moduleName = "tracekitchen.testapp.app-entrypoint.main"
                    workingDirectory = rootProject.rootDir.absolutePath
                    programParameters = ""
                }
                create<Gradle>("build project") {
                    taskNames = listOf("spotlessApply build")
                    projectPath = rootProject.rootDir.absolutePath
                }
                create<Gradle>("test project") {
                    taskNames = listOf("check --rerun-tasks")
                    projectPath = rootProject.rootDir.absolutePath
                }
            }
        }
    }
}
