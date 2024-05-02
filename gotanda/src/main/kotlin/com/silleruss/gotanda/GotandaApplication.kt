package com.silleruss.gotanda

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GotandaApplication

fun main(args: Array<String>) {
    runApplication<GotandaApplication>(*args)
}
