plugins {
    kotlin("jvm")
    application
}

group = "me.konyaco.pixivutil"
version = "0.4"

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
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.3")
}

application {
    applicationName = "pixivutil"
    mainClass.set("me.konyaco.pixivutil.MainKt")
    applicationDefaultJvmArgs += "-Djava.net.useSystemProxies=true"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}