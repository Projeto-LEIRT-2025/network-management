package com.github.project.networkservice.dto

import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRouterDto(

    @field:NotBlank
    val vendor: String,

    @field:NotBlank
    val model: String,

    @IpAddress
    @SerialName("ip_address")
    val ipAddress: String

)
