plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

application {
    mainClass = "hireline.MainKt"
}

tasks.named<JavaExec>("run") {
    val envFile = file(".env")
    if (envFile.exists()) {
        envFile.readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .forEach { line ->
                val key = line.substringBefore("=").trim()
                require("=" in line && key.isNotEmpty()) { ".env: expected KEY=value, got: $line" }
                if (System.getenv(key) == null) environment(key, line.substringAfter("=").trim())
            }
    }
}

dependencies {
    implementation(libs.javalin)
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.slf4j.simple)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.testcontainers.postgresql)
}

tasks.test {
    useJUnitPlatform()
    systemProperty("pgvectorImage", libs.versions.pgvector.image.get())
    testLogging { events("passed", "skipped", "failed") }
}
