import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("nu.studer.jooq") version "9.0"
    `maven-publish`
}

group = "pay.token"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    jooqGenerator("com.h2database:h2:2.3.232")
}

jooq {
    version = "3.19.15"
    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.h2.Driver"
                    url = "jdbc:h2:tcp://localhost/~/test"
                    user = "sa"
                    password = "1234"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    strategy.apply {
                        name = "org.jooq.codegen.DefaultGeneratorStrategy"
                    }
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        includes = ".*"
                        excludes = "INFORMATION_SCHEMA.*"
                    }
                    target.apply {
                        packageName = "pay.token.jooq.schema"
                        directory = "src/main/java"
                    }
                }
            }
        }
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "pay.token"
            artifactId = "pay-token-jooq"
            version = "0.0.1"
            from(components["java"])
        }
    }
}

//publishing {
//    publications {
//        register("mavenJava", MavenPublication::class) {
//            from(components["java"])
//        }
//    }
//}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier = ""
}

tasks.named("generateJooq").configure {
    // disable auto generation jooq
    enabled = false
}
