package com.github.project.webapplication.dto

import com.github.project.api.router.response.NetworkInterface
import kotlinx.serialization.Serializable

@Serializable
data class NetworkInterfaceDto(

    val name: String,
    val status: NetworkInterface.OperationalStatus = NetworkInterface.OperationalStatus.UNKNOWN

    )
