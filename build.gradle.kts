plugins {
    kotlin("jvm") version "1.9.25"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("AppKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // Dropwizard core + JDBI3
    implementation("io.dropwizard:dropwizard-core:2.1.10")
    implementation("io.dropwizard:dropwizard-jdbi3:2.1.10")

    // PostgreSQL JDBC driver
    implementation("org.postgresql:postgresql:42.7.2")

    // Jackson Kotlin support
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2") // adjust version

    // Unit testing
    testImplementation(kotlin("test"))
    testImplementation("io.dropwizard:dropwizard-testing:2.1.10")
}

tasks.test {
    useJUnitPlatform()
}
tasks.named<JavaExec>("run") {
    // Default program arguments for Dropwizard
    args("server", "src/main/resources/config.yml")
    jvmArgs = listOf("-Duser.timezone=Asia/Kolkata")
}

kotlin {
    jvmToolchain(17)
}
