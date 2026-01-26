plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.2.2"
}

group = "fi.sulku.hytale.consolespamfilter"
version = "1.1"

val hytaleVersion = "2026.01.24-6e2d4fc36"

repositories {
    mavenCentral()
    maven("https://maven.hytale.com/release")
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("com.hypixel.hytale:Server:${hytaleVersion}")
    compileOnly("org.jetbrains:annotations:24.1.0")
}

kotlin {
    jvmToolchain(25)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        exclude("org/jetbrains/annotations/**")
        relocate("kotlin", "fi.sulku.hytale.consolespamfilter.libs.kotlin")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/maven/**")
        minimize()
    }


    build {
        dependsOn(shadowJar)
    }
}
