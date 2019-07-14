import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  id("org.springframework.boot") version "2.1.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
  kotlin("jvm") version "1.2.71"
  kotlin("plugin.spring") version "1.2.71"
  jacoco
}

group = "com.feigntest"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

val bootJar: BootJar by tasks
bootJar.enabled = false

allprojects {
  repositories {
    mavenCentral()
  }

  tasks.withType<JacocoReport> {
    reports {
      xml.isEnabled = true
      html.isEnabled = true
    }
  }
}

subprojects {

  apply(plugin = "kotlin")
  apply(plugin = "kotlin-kapt")
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")

  buildscript {
    repositories {
      mavenCentral()
    }

    dependencies {
      classpath("org.kt3k.gradle.plugin:coveralls-gradle-plugin:2.8.2")
    }
  }

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.playtika.reactivefeign:feign-reactor-spring-configuration:1.0.30")
    implementation("com.playtika.reactivefeign:feign-reactor-webclient:1.0.30")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
      exclude(group = "junit", module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.projectreactor:reactor-test")
  }


  extra["springCloudVersion"] = "Greenwich.SR1"

  dependencyManagement {
    imports {
      mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "1.8"
    }
  }
}