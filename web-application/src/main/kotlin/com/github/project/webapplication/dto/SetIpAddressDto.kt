package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.validators.IpAddress
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetIpAddressDto(

    val credentials: CredentialsDto,

    @SerialName("ip_address")
    @IpAddress
    val ipAddress: String,

    @SerialName("interface_name")
    val interfaceName: String

)
