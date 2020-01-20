group = "com.atwzj"
version = "0.1-SNAPSHOT"

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.3")
    implementation("com.alibaba", "fastjson", "1.2.62")
    implementation("org.jsoup", "jsoup", "1.12.1")
}

tasks {
    jar {
        setProperty("archiveBaseName", "com.atwzj.pixivlib-core")
    }
}