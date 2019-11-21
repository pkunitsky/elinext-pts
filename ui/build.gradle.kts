import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.moowork.gradle.node.npm.NpmTask


buildscript {
  repositories {
    mavenCentral()
  }
}

plugins {
  id("com.bmuschko.docker-remote-api")
  id("com.moowork.node") version "1.3.1"
}

buildDir = file("$rootDir/docker/src/main/${project.name}/build")
group = "pts-microservice"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

tasks.register<NpmTask>("pts_build") {
  setNpmCommand("run", "build")
  dependsOn("npm_install")
}
tasks.register<Copy>("copy_dist") {
  from("$rootDir/ui/dist/pts-ui")
  into("$buildDir/")
  dependsOn("pts_build")
}

val buildMyAppImage by tasks.creating(DockerBuildImage::class) {
  inputDir.set(file("$rootDir/docker/src/main/${project.name}/"))
  tags.add("${project.name}:latest")
  dependsOn("copy_dist")
}
node {
  // Version of node to use.
  version = "10.13.0"

  // Version of npm to use.
  npmVersion = "6.9.0"

  // Base URL for fetching node distributions (change if you have a mirror).
  distBaseUrl = "https://nodejs.org/dist"

  // If true, it will download node using above parameters.
  // If false, it will try to use globally installed node.
  download = true

//  // Set the work directory for unpacking node
//  workDir = file("${project.buildDir}/nodejs")
//
//  // Set the work directory for NPM
//  npmWorkDir = file("${project.buildDir}/npm")
//
//  // Set the work directory where node_modules should be located
//  nodeModulesDir = file("${project.projectDir}")
}
task("build-services") {
  dependsOn(buildMyAppImage)
}



