pluginManagement {
    repositories {
        maven { url = uri("https://chaquo.com/maven") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "ScamDetectorApp"
include(":app")