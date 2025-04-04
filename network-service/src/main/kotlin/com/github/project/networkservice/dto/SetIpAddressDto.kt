package com.github.project.networkservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetIpAddressDto(

    @SerialName("router_id")
    val routerId: Long,

    val username: String,

    val password: String,

    @SerialName("ip_address")
    val ipAddress: String,

    @SerialName("interface_name")
    val interfaceName: String

)
