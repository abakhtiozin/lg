import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.31"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
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
//    maven (url = "https://github.com/intel-iot-devkit/tinyb")
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
//    compile("")
    compile("org.sputnikdev","bluetooth-manager-tinyb","1.3.2")
    // https://mvnrepository.com/artifact/org.sputnikdev/bluetooth-manager-tinyb
//    compile group: '', name: 'bluetooth-manager-tinyb', version: '1.3.2'
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}