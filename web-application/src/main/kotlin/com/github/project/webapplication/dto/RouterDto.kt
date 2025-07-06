package com.github.project.webapplication.dto

import com.github.project.webapplication.validators.IpAddress
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

@Serializable
data class UpdateRouterDto(

    @SerialName("ip_address")
    val ipAddress: String? = null,

    val model: String? = null,

    val vendor: String? = null

)

@Serializable
data class RouterDto(

    val id: Long,

    val model: String,

    val vendor: String,

    @SerialName("ip_address")
    val ipAddress: String

)
