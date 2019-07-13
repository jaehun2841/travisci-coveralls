package com.feigntest.demo.controller

import com.feigntest.democommon.domain.DemoData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class DemoController {

  @GetMapping("/demo/one")
  fun getOne(params: DemoParams): Mono<DemoData> {
    return Mono.just(DemoData(1, "Test1"))
  }

  @GetMapping("/demo/list")
  fun getList(): Mono<List<DemoData>> {
    return Mono.just(listOf(DemoData(1, "Test1"), DemoData(2, "Test2"), DemoData(3, "Test3")))
  }
}