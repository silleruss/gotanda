package com.silleruss.gotanda.core.advice

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonIncludeProperties
import org.springframework.http.HttpStatus

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(
    val isSuccess: Boolean,
    val data: T?,
    val customError: CustomError? = null
) {

    @JsonIncludeProperties("value", "message")
    data class CustomError(
        val value: HttpStatus,
        override val message: String?
    ) : Exception(message) {
        fun getValue() = this.value.value()
    }

}