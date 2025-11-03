val projectName: String by settings

rootProject.name = projectName

pluginManagement {
    val kotlinVersion: String by settings
    val shadowVersion: String by settings
    val runPaperVersion: String by settings
    val minotaurVersion: String by settings
    val paperweightUserdevVersion: String by settings
    val ktLintPluginVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.gradleup.shadow") version shadowVersion
        id("xyz.jpenilla.run-paper") version runPaperVersion
        id("com.modrinth.minotaur") version minotaurVersion
        id("io.papermc.paperweight.userdev") version paperweightUserdevVersion
        id("org.jlleitschuh.gradle.ktlint") version ktLintPluginVersion
    }
}
