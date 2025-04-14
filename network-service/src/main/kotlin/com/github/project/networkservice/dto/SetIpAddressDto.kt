package com.github.project.networkservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetIpAddressDto(

    val credentials: CredentialsDto,

    @SerialName("ip_address")
    val ipAddress: String,

    @SerialName("interface_name")
    val interfaceName: String

)
