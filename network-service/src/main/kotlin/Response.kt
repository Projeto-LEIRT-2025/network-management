package com.github.projeto

data class Response<T>(

    val raw: String,
    val data: T

)