package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRouterDto(

    @field:NotBlank
    val vendor: String,

    @field:NotBlank
    val model: String,

    @field:NotBlank
    @SerialName("ip_address")
    val ipAddress: String

)
