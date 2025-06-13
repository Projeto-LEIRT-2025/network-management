package com.github.project.webapplication.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouterDto(

    val id: Long,

    val model: String,

    val vendor: String,

    @SerialName("ip_address")
    val ipAddress: String

)
