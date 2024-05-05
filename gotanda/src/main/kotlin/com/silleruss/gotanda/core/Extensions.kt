package com.silleruss.gotanda.core

import arrow.core.Either
import reactor.core.publisher.Mono
import java.util.*

inline fun <L, R: Any> Optional<R>.toEither(item: () -> L): Either<L, R> = if (isPresent) Either.Right(get()) else Either.Left(item())

fun <T: Any> T?.toOptional(): Optional<T> = this?.let { Optional.of(it) } ?: Optional.empty()

fun <T : Any> Either<Throwable, T>.toMono() = fold({ error: Throwable -> Mono.error(error) }) { data: T -> Mono.just(data) }
