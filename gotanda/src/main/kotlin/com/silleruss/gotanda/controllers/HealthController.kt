package com.silleruss.gotanda.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping
    fun get(): Mono<String> {
        return Mono.just("good")
    }

}