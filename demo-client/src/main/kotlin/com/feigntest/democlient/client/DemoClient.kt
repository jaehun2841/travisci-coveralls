package com.feigntest.democlient.client

import com.feigntest.democommon.domain.DemoData
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@ReactiveFeignClient(name="demoClient", url = "\${demo.url}")
interface DemoClient {

  @GetMapping(value = "/demo/one")
  fun getOne(@SpringQueryMap params: MutableMap<String, Any>): Mono<DemoData>

  @GetMapping(value = "/demo/list")
  fun getList(): Mono<List<DemoData>>
}