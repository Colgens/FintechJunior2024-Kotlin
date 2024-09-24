plugins {
    kotlin("jvm") version "2.0.0-RC1"
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0-RC1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation("io.ktor:ktor-client-serialization:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")
    implementation("ch.qos.logback:logback-classic:1.5.8")
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-client-mock:2.3.12")
    implementation("com.opencsv:opencsv:5.9")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}