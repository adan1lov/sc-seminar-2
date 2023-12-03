plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(group="commons-io", name="commons-io", version= "2.15.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest.attributes["Main-Class"] = application.mainClass

    configurations["compileClasspath"].forEach {
        from(zipTree(it.absolutePath))
    }
}
