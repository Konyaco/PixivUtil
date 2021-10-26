plugins {
    kotlin("jvm")
    application
}

group = "me.konyaco.pixivutil"
version = "0.2-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")
    implementation(group = "com.alibaba", name = "fastjson", version = "1.2.76")
    implementation(group = "org.jsoup", name = "jsoup", version = "1.14.3")
    implementation(group = "io.ktor", name = "ktor-client-java", version = "1.6.4")
}

application {
    mainClass.set("me.konyaco.pixivutil.MainKt")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
}