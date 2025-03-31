plugins {
    kotlin("jvm") version libs.versions.kotlin
    kotlin("plugin.serialization") version libs.versions.kotlin
    kotlin("plugin.spring") version libs.versions.kotlin
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
}

val springCloudVersion by extra("2024.0.0")

group = "com.github.project"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation(libs.serialization)
    implementation(libs.reflection)
    implementation(libs.spring.boot)
    implementation(libs.spring.boot.web)
    implementation(libs.spring.data.jpa)
    implementation(libs.jakarta.validation)
    implementation(libs.postgresql)
    testImplementation(libs.spring.boot.test)
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}