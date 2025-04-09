package com.github.project.networkservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StaticRouteDto(

    val username: String,
    val password: String,
    val gateway: String,

    @SerialName("ip_address")
    val ipAddress: String

)
