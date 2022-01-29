plugins {
    kotlin("jvm")
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.14.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
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
                name.set("Aliyun Library")
                description.set("Aliyun Basic Library.")
                url.set("https://github.com/yisen-cai/spring-aliyun/tree/main/aliyun-library")
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