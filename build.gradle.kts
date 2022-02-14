buildscript {
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.6.10"
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
}


tasks.register("clean").configure {
    delete(rootProject.buildDir)
}