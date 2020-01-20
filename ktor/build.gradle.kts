group = "com.atwzj"
version = "0.1-SNAPSHOT"

dependencies {
    implementation(project(":core"))
    implementation("io.ktor", "ktor-client-core-jvm", "1.3.0")
    implementation("io.ktor", "ktor-client-apache", "1.3.0")
}

tasks {
    jar {
        setProperty("archiveBaseName", "com.atwzj.pixivlib-ktor")
    }
}