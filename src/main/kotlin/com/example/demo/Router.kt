package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@RestController
internal class AnnotatedController(private val entityDao: EntityDao) {

    @GetMapping
    fun get(): Mono<Entity> = entityDao.findById(UUID.randomUUID())
//        .timeout(Duration.ofMillis(100))

}
