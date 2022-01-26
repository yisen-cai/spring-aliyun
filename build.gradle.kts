plugins {
    kotlin("jvm") version "1.5.10"
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
}


val releaseRepoUrl by extra("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
val snapshotRepoUrl by extra("https://oss.sonatype.org/content/repositories/snapshots/")


allprojects {
    group = "com.glancebar.aliyun"
    version = "0.0.6-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}