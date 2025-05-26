plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
}

group = "com.github.project"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly(project(":api"))
    compileOnly(project(":network-service"))

    compileOnly(libs.serialization)
    compileOnly(libs.reflection)
    compileOnly(libs.spring.data.jpa)
    compileOnly(libs.postgresql)

    testImplementation(kotlin("test"))
    testImplementation(project(":network-service"))
}

kotlin {

    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }

    jvmToolchain(23)
}
