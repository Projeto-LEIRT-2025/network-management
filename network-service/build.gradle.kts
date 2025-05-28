plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
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
    compileOnly(libs.serialization)
    compileOnly(libs.reflection)
    compileOnly(libs.spring.data.jpa)
    compileOnly(libs.spring.boot.validation)
    compileOnly(libs.postgresql)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.data.jpa)
    testImplementation(project(":api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {

    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }

    jvmToolchain(23)
}