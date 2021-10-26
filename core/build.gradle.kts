plugins {
    kotlin("jvm")
}

dependencies {
    api("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")
    api("io.ktor", "ktor-client-core", "1.6.4")
    implementation("com.alibaba", "fastjson", "1.2.76")
    implementation("org.jsoup", "jsoup", "1.14.3")

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.ktor", "ktor-client-java", "1.6.4")
}

tasks {
    jar {
        setProperty("archiveBaseName", "me.konyaco.pixivlib-core")
    }
    test {
        useJUnitPlatform()
    }
}
