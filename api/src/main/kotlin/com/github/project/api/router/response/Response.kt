package com.github.project.api.router.response

data class Response<T>(

    val raw: String,
    val data: T

)