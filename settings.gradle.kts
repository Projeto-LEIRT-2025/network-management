plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "network-management"
include("network-service")
include("plugin")
include("api")
include("metrics-service")
include("web-application")
