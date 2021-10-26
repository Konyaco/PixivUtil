plugins {
    kotlin("jvm") version "1.5.31"
}

group = "me.konyaco.pixivlib"
version = "0.1-SNAPSHOT"

allprojects {
    apply {
        plugin("kotlin")
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.3")
    }
    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "11"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "11"
        }
    }
}
