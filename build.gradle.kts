plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
    id("com.modrinth.minotaur")
    id("io.papermc.paperweight.userdev")
    id("org.jlleitschuh.gradle.ktlint")
}

val targetJavaVersion = 21

val projectName: String by project
val pluginVersion: String by project
val minecraftMinor: String by project
val minecraftPatch: String by project
val paperApiSubVersion: String by project

val mcMinor = System.getenv("LBC_BUILD_MC_MINOR") ?: minecraftMinor
val mcPatch = System.getenv("LBC_BUILD_MC_PATCH") ?: minecraftPatch

val minecraftVersion = "1.$mcMinor.$mcPatch"

val supportedMinecraftVersions = listOf("1.21.8", "1.21.9", "1.21.10")

group = "dev.schnelle"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc-repo" }
}

dependencies {
    paperweight.paperDevBundle("$minecraftVersion-$paperApiSubVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(kotlin("reflect"))
}

runPaper.folia.registerTask {
    minecraftVersion(minecraftVersion)
    runDirectory(file("run/folia"))
}

tasks {
    runServer {
        minecraftVersion(minecraftVersion)
        runDirectory(file("run/paper"))
    }

    jar {
        destinationDirectory.set(file("${layout.buildDirectory.get()}/libs/leanJar/"))
        archiveBaseName.set("$projectName-$pluginVersion-lean")
    }

    shadowJar {
        destinationDirectory.set(file("${layout.buildDirectory.get()}/jar"))
        archiveFileName.set("$projectName-$pluginVersion.jar")
    }

    build {
        dependsOn("shadowJar")
        dependsOn("ktlintCheck")
    }

    processResources {
        val props =
            mapOf(
                "version" to pluginVersion,
                "minecraftVersion" to minecraftVersion,
                "projectName" to projectName,
            )
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") { expand(props) }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("locatorbarconfiguration")
    versionNumber.set(pluginVersion)
    versionType.set("release")
    uploadFile.set(tasks.shadowJar)
    gameVersions.addAll(supportedMinecraftVersions)
    loaders.addAll("paper", "folia")
}

kotlin { jvmToolchain(targetJavaVersion) }
