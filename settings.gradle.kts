rootProject.name = "pts-microservices"

include("config-service", "registry-service", "gateway", "rest", "ui")

val springBootPluginVersion: String by settings
val springDependencyManagementPluginVersion: String by settings
val dockerRemoteApiVersion: String by settings
val liquibasePluginVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootPluginVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementPluginVersion)
                "com.bmuschko.docker-remote-api" -> useVersion(dockerRemoteApiVersion)
                "org.liquibase.gradle" -> useVersion(liquibasePluginVersion)
            }
        }
    }
}