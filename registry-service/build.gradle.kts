import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val springCloudVersion: String by project
val springCloudStarterNetflixEurekaServer: String by project
val springCloudConfigClient: String by project
val junitVersion: String by project
val jaxbApiVersion: String by project
val jaxbRuntimeVersion: String by project
val javaxActivationVersion: String by project

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

application {
    applicationName = "registry-service"
    mainClassName = "com.pts.eureka.EurekaServerApp"
}

buildDir = file("$rootDir/docker/src/main/${project.name}/build")
group = "pts-microservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-server", springCloudStarterNetflixEurekaServer)
    implementation("org.springframework.cloud", "spring-cloud-config-client", springCloudConfigClient)
    implementation("javax.xml.bind", "jaxb-api", jaxbApiVersion)
    implementation("javax.activation", "activation", javaxActivationVersion)
    implementation("org.glassfish.jaxb", "jaxb-runtime", jaxbRuntimeVersion)
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



