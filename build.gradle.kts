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
