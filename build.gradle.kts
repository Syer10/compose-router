import com.vanniktech.maven.publish.MavenPublishPluginExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    kotlin("jvm") version "1.4.32"
    id("org.jetbrains.compose") version "0.4.0-build190"
    id("com.vanniktech.maven.publish") version "0.15.1"
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

val mavenPublish = extensions["mavenPublish"] as MavenPublishPluginExtension
mavenPublish.sonatypeHost = SonatypeHost.S01

// Read in the signing.properties file if it is exists
val signingPropsFile = rootProject.file("release/signing.properties")
if (signingPropsFile.exists()) {
    Properties().apply {
        signingPropsFile.inputStream().use {
            load(it)
        }
    }.forEach { key1, value1 ->
        val key = key1.toString()
        val value = value1.toString()
        if (key == "signing.secretKeyRingFile") {
            // If this is the key ring, treat it as a relative path
            project.ext.set(key, rootProject.file(value).absolutePath)
        } else {
            project.ext.set(key, value)
        }
    }
}