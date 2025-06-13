package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticRouteDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotNull
    val gateway: String,

    @SerialName("ip_address")
    @IpAddress
    val ipAddress: String,

    @field:NotNull
    @field:Min(0)
    @field:Max(32)
    val mask: Int

)
