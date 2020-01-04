plugins {
    kotlin("jvm") version "1.3.61"
}

group = "com.atwzj"
version = "0.2-SNAPSHOT"

allprojects {
    apply {
        plugin("kotlin")
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }
    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "1.6"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "1.6"
        }
    }
}

tasks {
    build {
    }
}