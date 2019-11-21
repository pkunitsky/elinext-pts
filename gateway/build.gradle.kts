import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val springCloudStarterNetflixEurekaClient: String by project
val springCloudVersion: String by project
val springCloudStarterNetflixZuul: String by project
val springBootWebStarter: String by project
val springCloudConfigClient: String by project
val springBootSecurity: String by project
val springBootOauthResourceServer: String by project
val junitVersion: String by project
val thymeleafExtrasSpringSecurity5: String by project

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
}

group = "pts-microservice"
version = "1.0-SNAPSHOT"
buildDir = file("$rootDir/docker/src/main/${project.name}/build")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-zuul", springCloudStarterNetflixZuul)
    implementation("org.springframework.boot", "spring-boot-starter-web", springBootWebStarter)
    implementation("org.springframework.cloud", "spring-cloud-config-client", springCloudConfigClient)
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client", springCloudStarterNetflixEurekaClient)
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-resource-server", springBootOauthResourceServer)
    implementation("org.springframework.boot", "spring-boot-starter-security", springBootSecurity)
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
    applicationName = "gateway"
    mainClassName = "com.pts.gateway.GatewayApp"
}

tasks.withType<Jar> {
    destinationDir = file("$rootDir/docker/src/main/${project.name}/")
}

val build: DefaultTask by tasks
val jar: DefaultTask by tasks

val buildMyAppImage by tasks.creating(DockerBuildImage::class) {
    inputDir.set(file("$rootDir/docker/src/main/${project.name}/"))
    tags.add("${project.name}:latest")
}

task("build-services") {
    dependsOn(build, jar, buildMyAppImage)
}

