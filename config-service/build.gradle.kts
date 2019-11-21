import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val springCloudVersion: String by project
val springCloudConfigServer: String by project
val junitVersion: String by project
val dbUrl: String by project
val dbUser: String by project
val dbPassword: String by project
val dbChangelogFile: String by project
val dbLogLevel: String by project
val postgresqlDriverVersion: String by project
val liquibaseCore: String by project
val logbackCore: String by project
val logbackClassic: String by project

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    application
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.bmuschko.docker-remote-api")
    id("org.liquibase.gradle")
}

buildDir = file("$rootDir/docker/src/main/${project.name}/build")
group = "pts-microservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-config-server", springCloudConfigServer)
    liquibaseRuntime("org.postgresql", "postgresql", postgresqlDriverVersion)
    liquibaseRuntime("org.liquibase", "liquibase-core", liquibaseCore)
    liquibaseRuntime("ch.qos.logback", "logback-core", logbackCore)
    liquibaseRuntime("ch.qos.logback", "logback-classic", logbackClassic)
    testCompile("junit", "junit", junitVersion)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

application {
    applicationName = "config"
    mainClassName = "com.pts.config.ConfigServerApp"
}

tasks.withType<Jar> {
    destinationDir = file("$rootDir/docker/src/main/${project.name}/")
}

val buildMyAppImage by tasks.creating(DockerBuildImage::class) {
    inputDir.set(file("$rootDir/docker/src/main/${project.name}/"))
    tags.add("${project.name}:latest")
}

val build: DefaultTask by tasks
val jar: DefaultTask by tasks

task("build-services") {
    dependsOn(build, jar, buildMyAppImage)
}


liquibase {
    activities.register("main") {
        this.arguments = mapOf(
                "logLevel" to dbLogLevel,
                "changeLogFile" to dbChangelogFile,
                "url" to dbUrl,
                "username" to dbUser,
                "password" to dbPassword
        )
    }
    runList = "main"
}







