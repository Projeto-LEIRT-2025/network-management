plugins {
    kotlin("jvm") version libs.versions.kotlin
}

group = "com.github.project"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation(libs.snakeyaml)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}