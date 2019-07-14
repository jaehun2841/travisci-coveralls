import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  id("org.springframework.boot") version "2.1.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
  kotlin("jvm") version "1.2.71"
  kotlin("plugin.spring") version "1.2.71"
  jacoco
  id("com.github.kt3k.coveralls") version "2.8.2"
}

group = "com.feigntest"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

val bootJar: BootJar by tasks
bootJar.enabled = false

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.50")
    classpath("org.kt3k.gradle.plugin:coveralls-gradle-plugin:2.8.2")
  }
}

allprojects {
  repositories {
    mavenCentral()
    jcenter()
  }

  apply(plugin = "com.github.kt3k.coveralls")
  apply(plugin = "jacoco")
}

subprojects {

  apply(plugin = "kotlin")
  apply(plugin = "kotlin-kapt")
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")

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

  // setting for coveralls
  val jacocoTestReport: JacocoReport by tasks
  jacocoTestReport.reports {
    html.isEnabled = true // human readable
    xml.isEnabled = true // required by coveralls
  }
}

val jacocoRootReport by tasks.creating(JacocoReport::class) {
  description = "Generates an aggregate report from all subprojects"

  val sourceDirectoriesPaths = ArrayList<Any>()
  val classDirectoriesPaths = ArrayList<Any>()
  val executionDataPaths = ArrayList<Any>()

  for (project in subprojects) {
    dependsOn(project.tasks.test)
    sourceDirectoriesPaths.add(project.sourceSets.main.get().allSource.srcDirs)
    classDirectoriesPaths.add(project.sourceSets.main.get().output)
    executionDataPaths.add(project.tasks.jacocoTestReport.get().executionData)
  }
  val sourceDirectories = files(*sourceDirectoriesPaths.toArray())
  val classDirectories = files(*classDirectoriesPaths.toArray())
  val executionData = files(*executionDataPaths.toArray())

  reports {
    html.isEnabled = true // human readable
    xml.isEnabled = true // required by coveralls
  }
}

coveralls {

  val sourceDirectoriesPaths = ArrayList<Set<File>>()
  for (project in subprojects) {
    sourceDirectoriesPaths.add(project.sourceSets.main.get().allSource.srcDirs)
  }
  sourceDirs = sourceDirectoriesPaths.flatMap { files -> files.map { it.absolutePath } }
  jacocoReportPath = "${buildDir}/reports/jacoco/jacocoRootReport/jacocoRootReport.xml"
}
