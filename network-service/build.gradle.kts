plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.github.projeto"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.commons.net)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}