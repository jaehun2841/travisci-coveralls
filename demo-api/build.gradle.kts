import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
bootJar.mainClassName = "com.feigntest.demo.DemoApplication"
jar.enabled = true

dependencies {

  implementation(project(":demo-common"))
}