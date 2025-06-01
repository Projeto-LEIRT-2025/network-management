plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.shadow)
}

group = "com.github.projeto"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.commons.net)
    implementation(libs.snmp4j)

    implementation(project(":api"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}