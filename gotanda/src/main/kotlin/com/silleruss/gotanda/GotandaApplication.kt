package com.silleruss.gotanda

import com.silleruss.gotanda.core.advice.ResponseBodyAdvice
import jakarta.annotation.Resource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.accept.RequestedContentTypeResolver

@SpringBootApplication
class GotandaApplication {

    @Resource
    lateinit var serverCodecConfigurer: ServerCodecConfigurer

    @Resource
    lateinit var requestedContentTypeResolver: RequestedContentTypeResolver

    @Bean
    fun responseWrapper(): ResponseBodyAdvice = ResponseBodyAdvice(
        serverCodecConfigurer.writers, requestedContentTypeResolver
    )

}

fun main(args: Array<String>) {
    runApplication<GotandaApplication>(*args)
}
