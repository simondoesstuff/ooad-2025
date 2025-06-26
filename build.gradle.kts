import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" // Or the latest stable version
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // --- COMPILE DEPENDENCIES ---
    // proj lombok adds utility annotations such as @getter & @setter
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    // guava provides a wonderful event bus
    implementation("com.google.guava:guava:20.0")

    // --- TEST DEPENDENCIES ---
    // This is the primary dependency for writing JUnit 5 tests.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")

    // This is required for actually running the tests.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // These are for Lombok annotations within your test sources.
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// --- Configure the test task to use JUnit Platform ---
tasks.withType<Test> {
    useJUnitPlatform()

    // Optional: This shows test results in the console as they run.
    testLogging {
        events("passed", "skipped", "failed")
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

        configurations = listOf(project.configurations.runtimeClasspath.get())

        from(sourceSets.main.get().output) {
            include("ooad/$name/**")
            // include("ooad/common/**") // Uncomment if you have shared code
        }

        manifest {
            attributes("Main-Class" to mainPath)
        }
    }
}

tasks.register("buildJars") {
    group = "Build"
    description = "Builds all standalone, executable JARs."
    dependsOn(tasks.withType<ShadowJar>())
}
