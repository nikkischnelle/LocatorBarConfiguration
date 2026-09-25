val projectName: String = providers.gradleProperty("projectName").get()

rootProject.name = projectName
