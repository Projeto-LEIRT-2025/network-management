plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.shadow)
}

group = "com.github.project"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":api"))
    implementation(project(":network-service"))
    implementation(project(":metrics-service"))
    implementation(libs.serialization)
    implementation(libs.reflection)
    implementation(libs.spring.boot)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.thymeleaf)
    implementation(libs.postgresql)

    testImplementation(libs.spring.boot.test)
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