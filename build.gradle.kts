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
    mainClass = "hireline.platform.MainKt"
}

dependencies {
    implementation(libs.postgresql)
    implementation(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    implementation(libs.slf4j.api)
    runtimeOnly(libs.slf4j.simple)
}
