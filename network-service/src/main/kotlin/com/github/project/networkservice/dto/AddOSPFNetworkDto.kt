package com.github.project.networkservice.dto

import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class AddOSPFNetworkDto(

    val credentials: CredentialsDto,

    @IpAddress
    val network: String,

    @field:Min(0)
    @field:Max(32)
    @field:NotNull
    val mask: Int,

    @field:NotBlank
    val areaName: String

)