plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.sparepartfinance"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // Database
    implementation("com.h2database:h2:2.2.224")
    
    // JSON handling (for potential API)
    implementation("com.google.code.gson:gson:2.10.1")
}

javafx {
    version = "21.0.7"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("org.example.Main")
}