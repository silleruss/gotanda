package com.silleruss.gotanda.core.advice

import org.springframework.http.HttpStatus
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.accept.RequestedContentTypeResolver
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ResponseBodyAdvice(writers: MutableList<HttpMessageWriter<*>>, resolver: RequestedContentTypeResolver) :
    ResponseBodyResultHandler(writers, resolver) {

    override fun supports(result: HandlerResult): Boolean =
        (result.returnType.resolve() == Mono::class.java) || (result.returnType.resolve() == Flux::class.java)

    @Throws(ClassCastException::class)
    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        val body = result.returnValue
            .let { v ->
                when (v) {
                    is Mono<*> -> v
                    is Flux<*> -> v.collectList()
                    else -> throw ClassCastException("The \"body\" should be Mono<*> or Flux<*>!")
                }
            }
            .map { r -> Response(true, r, null) }
            .doOnError(logger::error)
            .onErrorMap { e ->
                if (e !is Response.CustomError)
                    Response.CustomError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
                else e
            }
            .doOnError { e -> exchange.response.statusCode = (e as Response.CustomError).value }
            .onErrorResume { e -> Mono.just(Response(false, null, e as Response.CustomError)) }

        return writeBody(body, result.returnTypeSource, exchange)
    }

}