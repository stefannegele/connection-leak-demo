package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration
import kotlin.concurrent.thread

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    runStressTest()
}

private val logger = LoggerFactory.getLogger("StressTest")

private fun runStressTest() {
    repeat(5) { runStressTestRequestThread(it) }
}

private fun runStressTestRequestThread(num: Int) {
    thread(name = "stress-test-$num") {
        val client = WebClient.create("http://localhost:8080/")

        for (i in (1..1_000)) {
            runCatching {
                client.get().retrieve().bodyToMono<Entity>()
                    .timeout(Duration.ofMillis(15)) // try to cancel request before answer
                    .block()
            }.onFailure {
                logger.debug(it.message, it)
            }
        }

        logger.info("done")
    }
}
