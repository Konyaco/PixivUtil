group = "com.atwzj"
version = "1.0"

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.3")
}

tasks {
    jar {
        setProperty("archiveBaseName", "com.atwzj.pixivlib-jvm")
    }
}