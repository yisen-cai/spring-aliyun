plugins {
    kotlin("jvm") version "1.5.10"
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}


val springBootVersion = properties["spring-boot-version"] as String


dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

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