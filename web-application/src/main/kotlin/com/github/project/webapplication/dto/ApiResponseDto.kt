package com.github.project.webapplication.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto<T>(

    val message: String,
    val data: T

)
