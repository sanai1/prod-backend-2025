val exposed_version: String by project
val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val hikaricp_version: String by project


plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

group = "ru.kotleteri"
version = "0.0.1"

application {
    mainClass.set("ru.kotleteri.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // ktor server
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-swagger")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-rate-limit")
    implementation("io.ktor:ktor-server-html-builder")
    implementation("io.ktor:ktor-server-forwarded-header")


    // docs
    implementation("io.bkbn:kompendium-core:4.0.3")

    // ktor client
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")

    // database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-migration:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")


    //s3
    implementation("software.amazon.awssdk:s3:2.20.103")

    // redis
    implementation("io.github.crackthecodeabhi:kreds:0.9.1")

    // hash
    implementation("de.nycode:bcrypt:2.2.0")
    implementation("org.mindrot:jbcrypt:0.4")

    // logs
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // tests
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("com.h2database:h2:2.3.232")
}

tasks.register("copyDependencies") {
    doLast {
        val libsDir = File("$buildDir/libs/libraries")
        libsDir.mkdirs()

        configurations.getByName("runtimeClasspath").files.forEach {
            if (it.name.endsWith(".jar")) {
                it.copyTo(File(libsDir, it.name))
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}