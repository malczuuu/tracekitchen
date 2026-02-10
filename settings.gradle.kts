pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}

rootProject.name = "tracekitchen"

include(":testapp:app-downstream")
include(":testapp:app-entrypoint")
include(":testapp:library-bom")
include(":testapp:library-common")

include(":tracekit")

include(":tracekit-boot4:tracekit-boot4-aspect")
include(":tracekit-boot4:tracekit-boot4-core")
include(":tracekit-boot4:tracekit-boot4-restclient")
include(":tracekit-boot4:tracekit-boot4-starter")
include(":tracekit-boot4:tracekit-boot4-webmvc")

verifyProjectNameDuplicates(rootProject)
changeChildrenBuildFileNames(rootProject)

/**
 * Duplicate project names have some side effects. This function verifies that there are no duplicate project names in
 * the build. Throws an exception if duplicates are found.
 */
fun verifyProjectNameDuplicates(project: ProjectDescriptor) {
    val projectsByName = mutableMapOf<String, MutableList<ProjectDescriptor>>()

    collectProjectNames(project, projectsByName)

    val duplicates = projectsByName.filterValues { it.size > 1 }

    check(duplicates.isEmpty()) {
        buildString {
            appendLine("Duplicate project names are not allowed:")
            duplicates.forEach { (name, projects) ->
                appendLine("  '$name' used by: ${projects.joinToString { it.path }}")
            }
        }
    }
}

/**
 * Collects project names into the provided map.
 */
fun collectProjectNames(project: ProjectDescriptor, names: MutableMap<String, MutableList<ProjectDescriptor>>) {
    names.computeIfAbsent(project.name) { mutableListOf() }.add(project)
    project.children.forEach { collectProjectNames(it, names) }
}

/**
 * Changes build file names of all child projects to `<project-name>.gradle.kts`.
 */
fun changeChildrenBuildFileNames(project: ProjectDescriptor) {
    project.children.forEach { changeChildrenBuildFileName(it) }
}

/**
 * Changes build file name of the given project and all its children to `<project-name>.gradle.kts`.
 */
fun changeChildrenBuildFileName(project: ProjectDescriptor) {
    project.buildFileName = "${project.name}.gradle.kts"
    project.children.forEach { child -> changeChildrenBuildFileName(child) }
}
