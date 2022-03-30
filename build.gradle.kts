buildscript {
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.6.10"
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
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

    ktlint {
        additionalEditorconfigFile.set(file("${rootProject.projectDir}/.editorconfig"))
    }
}