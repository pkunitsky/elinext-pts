import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val springCloudVersion: String by project
val springCloudStarterNetflixEurekaClient: String by project
val springCloudConfigClient: String by project
val springBootDataJpaStarter: String by project
val springBootJerseyStarter: String by project
val springBootWebStarter: String by project
val springBootHateoasStarter: String by project
val springBootStarterDataRest: String by project
val springBootStarterTest: String by project
val apachePoi: String by project
val apachePoiOoxml: String by project
val apacheLog4jCore: String by project
val junitVersion: String by project
val postgresqlDriverVersion: String by project
val lombok: String by project
val modelMapper: String by project
val javers: String by project
val swagger: String by project
val apacheCommonsIO: String by project

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    application
    id("com.bmuschko.docker-remote-api")
}

buildDir = file("$rootDir/docker/src/main/${project.name}/build")
group = "pts-microservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud", "spring-cloud-starter-netflix-eureka-client", springCloudStarterNetflixEurekaClient)
    implementation("org.springframework.cloud", "spring-cloud-config-client", springCloudConfigClient)
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa", springBootDataJpaStarter)
    implementation("org.springframework.boot", "spring-boot-starter-jersey", springBootJerseyStarter)
    implementation("org.springframework.boot", "spring-boot-starter-web", springBootWebStarter)
    implementation("org.springframework.boot", "spring-boot-starter-hateoas", springBootHateoasStarter)
    implementation("org.apache.poi", "poi", apachePoi)
    implementation("org.apache.poi", "poi-ooxml", apachePoiOoxml)
    implementation("org.apache.logging.log4j", "log4j-core", apacheLog4jCore)
    implementation("org.postgresql", "postgresql", postgresqlDriverVersion)
    implementation("org.postgresql", "postgresql", postgresqlDriverVersion)
    implementation("org.modelmapper", "modelmapper", modelMapper)
    implementation("org.springframework.boot", "spring-boot-starter-data-rest", springBootStarterDataRest)
    implementation("org.javers", "javers-core", javers)
    implementation("io.springfox", "springfox-swagger2", swagger)
    implementation("io.springfox", "springfox-swagger-ui", swagger)
    implementation("commons-io", "commons-io", apacheCommonsIO)
    compileOnly("org.projectlombok", "lombok", lombok)
    annotationProcessor("org.projectlombok", "lombok", lombok)
    testCompile("org.springframework.boot", "spring-boot-starter-test", springBootStarterTest)
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
    applicationName = "rest"
    mainClassName = "com.elinext.pts.rest.RestApp"
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


