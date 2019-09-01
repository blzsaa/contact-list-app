group = "hu.blzsaa"
version = "1.0-SNAPSHOT"

plugins {
    id("com.diffplug.gradle.spotless") version "3.24.2"
    id("com.github.ben-manes.versions") version "0.22.0"
}

spotless {
    kotlin {
        target("**/src/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
