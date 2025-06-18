package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RemoveIpAddressDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotEmpty
    val identifiers: List<Int>

)

@Serializable
data class SetIpAddressDto(

    val credentials: CredentialsDto,

    @SerialName("ip_address")
    @IpAddress
    val ipAddress: String,

    @SerialName("interface_name")
    val interfaceName: String

)