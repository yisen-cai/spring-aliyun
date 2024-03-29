plugins {
    kotlin("jvm")
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

val springBootVersion = properties["spring-boot-version"] as String

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

dependencies {
    implementation(project(":aliyun-library"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Generate its dependencies bean(Conditional Metadata) under META-INF(META-INF/spring-autoconfigure-metadata.properties) folder
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    // Creating IDE friendly Configuration Metadata
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


java {
    withJavadocJar()
    withSourcesJar()
}


publishing {
    repositories {
        maven {
            name = "ossrh"
            url = uri(if (version.toString().endsWith("SNAPSHOT"))
                properties["snapshot-repo-url"] as String
            else
                properties["release.repo-url"] as String)
            credentials {
                username = properties["ossrh.username"] as String?
                password = properties["ossrh.password"] as String?
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("Spring Boot Starter Aliyun")
                description.set("Support some aliyun utils and integrate with Spring.")
                url.set("https://github.com/yisen-cai/spring-aliyun/tree/v0.0.6/spring-boot-autoconfigure")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0'")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("yisen-cai")
                        name.set("YISHEN CAI")
                        email.set("yisen614@gmail.com")
                        organization.set("Glancebar")
                        organizationUrl.set("https://glancebar.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/yisen-cai/spring-aliyun.git'")
                    developerConnection.set("scm:git:git@github.com:yisen-cai/spring-aliyun.git")
                    url.set("https://github.com/yisen-cai/spring-aliyun")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}