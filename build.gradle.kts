import org.jetbrains.kotlin.com.intellij.openapi.vfs.StandardFileSystems.jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.wrapper.Wrapper

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.61"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

plugins {
    java
}

group = "com.abakhtiozin"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
}

val kotlin_version: String by extra

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlin_version))
    compile(files("lib/tinyb.jar"))
    testCompile(files("lib/tinyb.jar"))
    compile(group = "com.google.code.gson", name = "gson", version = "2.8.5")
    compile(group = "org.bidib.com.serialpundit", name = "sp-tty", version = "1.0.4.1")
    compile(group = "org.bidib.com.serialpundit", name = "sp-core", version = "1.0.4")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.10"
    distributionType = Wrapper.DistributionType.ALL
}

task("java_native") {
    println("Check your java library path")
    System.getProperties()
            .filter {
                it.key.toString().contains("java.library.path")
            }
            .forEach {
                println(it)
            }
}