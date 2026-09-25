import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


val targetJavaVersion = 21
val buildJavaVersion = 25
group = "dev.schnelle"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc-repo" }
}

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.paperWeightUserdev)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.ktlint)
}

val projectName: String = property("projectName") as String
val pluginVersion: String = property("pluginVersion") as String

val supportedMinecraftVersions: String = property("supportedMinecraftVersions") as String
val supportedMinecraftVersionList = supportedMinecraftVersions.split(",")
val buildMinecraftVersion = supportedMinecraftVersionList.first()
val runMinecraftVersion = supportedMinecraftVersionList.last()

logger.lifecycle("Building against: $buildMinecraftVersion")
logger.lifecycle("Advertising support for versions:")
supportedMinecraftVersionList.forEach { version -> logger.lifecycle(" - $version") }

dependencies {
    paperweight.paperDevBundle("$buildMinecraftVersion-${libs.versions.paperApiRevision.get()}")
    implementation(libs.bstats)
    implementation(kotlin("reflect"))
}

runPaper.folia.registerTask {
    minecraftVersion(runMinecraftVersion)
    runDirectory(file("run/folia"))
}

tasks {
    runServer {
        minecraftVersion(runMinecraftVersion)
        runDirectory(file("run/paper"))
    }

    jar {
        destinationDirectory.set(file("${layout.buildDirectory.get()}/libs/leanJar/"))
        archiveBaseName.set("$projectName-$pluginVersion-lean")
    }

    shadowJar {
        destinationDirectory.set(file("${layout.buildDirectory.get()}/jar"))
        archiveFileName.set("$projectName-$pluginVersion.jar")

        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    build {
        dependsOn("shadowJar")
        dependsOn("ktlintCheck")
    }

    processResources {
        val props =
            mapOf(
                "version" to pluginVersion,
                "minecraftVersion" to buildMinecraftVersion,
                "projectName" to projectName,
            )
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") { expand(props) }
    }

    withType<ShadowJar> {
        relocate("org.bstats", "$group.bstats")
    }

    withType<JavaCompile>().configureEach {
        options.release.set(targetJavaVersion)
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget
                    .fromTarget(targetJavaVersion.toString()),
            )
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("locatorbarconfiguration")
    versionNumber.set(pluginVersion)
    versionType.set("release")
    uploadFile.set(tasks.shadowJar)
    gameVersions.addAll(supportedMinecraftVersionList)
    loaders.addAll("paper", "folia", "purpur")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set(libs.versions.ktlint.get())
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

kotlin {
    jvmToolchain(buildJavaVersion)
}
