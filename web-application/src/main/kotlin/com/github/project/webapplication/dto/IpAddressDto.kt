package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.webapplication.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoveIpAddressDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @SerialName("interface_name")
    @field:NotBlank
    val interfaceName: String

)

@Serializable
data class SetIpAddressDto(

    val credentials: CredentialsDto,

    @SerialName("ip_address")
    @IpAddress
    val ipAddress: String,

    @field:Min(0)
    @field:Max(32)
    val prefix: Int,

    @SerialName("interface_name")
    val interfaceName: String

)