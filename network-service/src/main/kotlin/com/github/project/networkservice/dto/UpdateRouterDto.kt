package com.github.project.networkservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRouterDto(

    @SerialName("ip_address")
    val ipAddress: String? = null,

    val model: String? = null,

    val vendor: String? = null

)