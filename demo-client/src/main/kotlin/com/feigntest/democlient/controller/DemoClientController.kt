package com.feigntest.democlient.controller

import com.feigntest.democlient.client.DemoClient
import com.feigntest.democommon.domain.DemoData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DemoClientController(
        val demoClient: DemoClient
) {

  @GetMapping("/api/demo/one")
  fun getOne(): Mono<DemoData> {
    return demoClient.getOne(mutableMapOf("id" to 1))
  }

  @GetMapping("/api/demo/list")
  fun getList(): Mono<List<DemoData>> {
    return demoClient.getList()
  }
}