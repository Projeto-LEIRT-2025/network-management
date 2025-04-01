package com.github.project.networkservice.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto<T>(

    val message: String,
    val data: T

)
