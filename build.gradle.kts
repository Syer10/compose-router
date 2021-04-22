import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.compose") version "0.4.0-build184"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    api(compose.runtime)

    testImplementation(kotlin("test-junit5"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    test {
        useJUnit()
    }
}

artifacts {
    archives(tasks.jar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.Syer10"
            artifactId = project.name
            version = "0.24.2-desktop"

            from(components["java"])
        }
    }
}