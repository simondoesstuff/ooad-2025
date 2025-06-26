import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" // Or the latest stable version
}

dependencies {
    // proj lombok adds utility annotations such as @getter & @setter
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    // guava provides a wonderful event bus
    implementation("com.google.guava:guava:20.0")
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
    "project2",
    "project3",
)

// This loop registers a custom ShadowJar task for each entrypoint.
entrypointNames.forEach { name ->
    val mainPath = "ooad.$name.Main"

    tasks.register<ShadowJar>("build-$name") {
        group = "Application"
        description = "Builds a standalone, executable JAR for $name"
        archiveFileName.set("$name.jar")

        // FIX #1: This includes external dependencies (like Guava) in the JAR.
        // The original script was missing this, so dependencies were not being packaged.
        // This line configures the task to use the 'runtimeClasspath', which contains
        // all your 'implementation' dependencies.
        configurations = listOf(project.configurations.runtimeClasspath.get())

        // FIX #2: This ensures only relevant source files are included for this entrypoint.
        // The original script used `from(sourceSets.main.get().output)` without filtering,
        // which copied ALL compiled classes into EVERY jar.
        // This `from` block now filters to include only the classes in the specific
        // package for this entrypoint (e.g., `ooad.project1a`).
        from(sourceSets.main.get().output) {
            include("ooad/$name/**")

            // NOTE: If you have common code shared between projects (e.g., in a package
            // like `ooad.common`), you will need to add an additional `include` for it:
            // include("ooad/common/**")
        }

        manifest {
            attributes("Main-Class" to mainPath)
        }
    }
}

tasks.register("buildJars") {
    group = "Build"
    description = "Builds all standalone, executable JARs."
    // This was correct. It depends on all tasks of type ShadowJar, which, because
    // the default is disabled, are only the ones generated in the loop above.
    dependsOn(tasks.withType<ShadowJar>())
}
