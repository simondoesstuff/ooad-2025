import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("com.gradleup.shadow") version "9.0.0-beta15"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// --- Disable Default Application Tasks ---
// Since we have no single default entrypoint, we disable the tasks
// from the 'application' plugin that would fail without one.
tasks.named("run") { enabled = false }
tasks.named("startScripts") { enabled = false }
tasks.named("installDist") { enabled = false }
tasks.named("shadowJar") { enabled = false }

// INFO: Entrypoints

val entrypointNames = listOf(
    "project1a",
    "project1b",
)

// automatically creates a run task for each name.
entrypointNames.forEach { name ->
    val mainPath = "ooad.$name.Main"

    tasks.register<ShadowJar>("build-$name") {
        group = "Application"
        description = "Runs the $mainPath entrypoint"
        archiveFileName = "$name.jar"

        from(sourceSets.main.get().output)
        manifest {
          attributes(mapOf("Main-Class" to mainPath))
        }
    }
}

tasks.register("buildJars") {
    group = "Build"
    description = "Builds all standalone, executable JARs."
    dependsOn(tasks.withType<ShadowJar>())
}
