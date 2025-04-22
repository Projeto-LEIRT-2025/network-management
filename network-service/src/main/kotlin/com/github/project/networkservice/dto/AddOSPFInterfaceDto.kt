package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddOSPFInterfaceDto(

    val credentials: CredentialsDto,

    @SerialName("interface_name")
    @field:NotBlank
    val interfaceName: String,

    @SerialName("area_name")
    @field:NotBlank
    val areaName: String,

    @SerialName("network_type")
    @field:NotBlank
    val networkType: String,

    @field:NotNull
    val cost: Int

)
