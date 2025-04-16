package com.github.project.networkservice.dto

import com.github.project.networkservice.validators.IpAddress
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticRouteDto(

    val credentials: CredentialsDto,
    val gateway: String,

    @SerialName("ip_address")
    @IpAddress
    val ipAddress: String

)
