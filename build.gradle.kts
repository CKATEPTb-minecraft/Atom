import proguard.gradle.ProGuardTask

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.2.1")
    }
}

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow").version("7.1.0")
    id("io.github.gradle-nexus.publish-plugin").version("1.1.0")
    // https://github.com/PaperMC/paperweight
    id("io.papermc.paperweight.userdev").version("1.3.8")
}

group = "dev.ckateptb.minecraft"
version = "1.3.1-SNAPSHOT"

val rootPackage = "${project.group}.${project.name.toLowerCase()}"
val internal = "${rootPackage}.internal"

repositories {
    mavenCentral()
    maven("https://repo.animecraft.fun/repository/maven-snapshots/")
}

dependencies {
    paperDevBundle("1.20.2-R0.1-SNAPSHOT")

    compileOnly("dev.ckateptb.minecraft:Nicotine:2.0.0-SNAPSHOT")
    compileOnly("dev.ckateptb.minecraft:Caffeine:1.0.0-SNAPSHOT")
    compileOnly("dev.ckateptb.minecraft:Varflex:1.0.2-SNAPSHOT")

    implementation("io.projectreactor:reactor-core:3.5.11")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks {
    shadowJar {
//        relocate("org.reactivestreams", "${internal}.reactivestreams")
//        relocate("reactor", "${internal}.reactor")
    }
    register<ProGuardTask>("shrink") {
        dependsOn(shadowJar)
        injars(shadowJar.get().outputs.files)
        outjars("${project.buildDir}/libs/${project.name}-${project.version}.jar")

        ignorewarnings()

        libraryjars("${System.getProperty("java.home")}/jmods")

        keep(mapOf("includedescriptorclasses" to true), "public class !${internal}.** { *; }")
        keepattributes("RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,RuntimeVisibleTypeAnnotations")

        dontobfuscate()
        dontoptimize()
    }
    build {
        // Uncomment next line if u need only embed, without shrink
        dependsOn(reobfJar, shadowJar)
        // Comment next line if u need only embed, without shrink
        // dependsOn(reobfJar, "shrink")
    }
    publish {
        // Uncomment next line if u need only embed
        dependsOn(reobfJar, shadowJar)
        // Comment next line if u need only embed, without shrink
        // dependsOn(reobfJar, "shrink")

    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    named<Copy>("processResources") {
        filesMatching("plugin.yml") {
            expand(
                "projectVersion" to project.version,
                "projectName" to project.name,
                "projectMainClass" to "${rootPackage}.${project.name}"
            )
        }
        from("LICENSE") {
            rename { "${project.name.toUpperCase()}_${it}" }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications {
        publications.create<MavenPublication>("mavenJava") {
            artifacts {
                artifact(tasks.getByName("shrink").outputs.files.singleFile)
            }
        }
    }
}

nexusPublishing {
    repositories {
        create("myNexus") {
            nexusUrl.set(uri("https://repo.animecraft.fun/"))
            snapshotRepositoryUrl.set(uri("https://repo.animecraft.fun/repository/maven-snapshots/"))
            username.set(System.getenv("NEXUS_USERNAME"))
            password.set(System.getenv("NEXUS_PASSWORD"))
        }
    }
}