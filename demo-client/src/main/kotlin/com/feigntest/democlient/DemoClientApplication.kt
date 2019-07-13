package com.feigntest.democlient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactivefeign.spring.config.EnableReactiveFeignClients

@EnableReactiveFeignClients
@SpringBootApplication
class DemoClientApplication

fun main(args: Array<String>) {
	runApplication<DemoClientApplication>(*args)
}
