buildscript {
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.8.10"
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("${rootProject.projectDir}/detekt.yml")
        buildUponDefaultConfig = true
    }
}