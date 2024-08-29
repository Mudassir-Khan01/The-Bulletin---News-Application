// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id ("com.android.library") version "7.4.1" apply false
    id ("androidx.navigation.safeargs") version "2.5.0" apply false
}
buildscript{
    repositories{
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.4.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        val nav_version = "2.5.0"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}