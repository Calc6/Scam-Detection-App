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
        maven { url = uri("https://chaquo.com/maven") }
        google()
        mavenCentral()
    }
}

rootProject.name = "ScamDetectorApp"
include(":app")