package com.feigntest.democlient

import com.feigntest.democlient.client.DemoClient
import com.feigntest.democommon.domain.DemoData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@SpringBootTest
class DemoClientControllerTest {

  @Autowired
  lateinit var applcationContext: ApplicationContext

  @MockBean
  lateinit var mockDemoClient: DemoClient

  lateinit var webTestClient: WebTestClient

  @BeforeEach
  fun init() {
    webTestClient = WebTestClient
            .bindToApplicationContext(applcationContext)
            .configureClient()
            .build()
    `when`(mockDemoClient.getOne(anyMap())).thenReturn(Mono.just(DemoData(1, "TEST DATA")))
  }

  @Test
  fun `should receive test demo data`() {
    val demo = webTestClient.get()
            .uri("/api/demo/one")
            .exchange()
            .expectStatus().isOk
            .expectBody(DemoData::class.java)
            .returnResult().responseBody
    assertThat(demo!!.id).isEqualTo(1)
    assertThat(demo.text).isEqualTo("TEST DATA")
  }
}