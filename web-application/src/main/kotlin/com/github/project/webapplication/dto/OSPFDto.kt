package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.webapplication.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CreateOSPFAreaDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotBlank
    val areaId: String,

    @field:NotBlank
    val processId: String

)

@Serializable
data class CreateOSPFProcessDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @SerialName("process_id")
    @field:NotBlank
    val processId: String,

    @SerialName("router_id")
    @field:NotBlank
    val routerId: String

)

@Serializable
data class AddOSPFInterfaceDto(

    @field:NotNull
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

@Serializable
data class AddOSPFNetworkDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @IpAddress
    val network: String,

    @field:Min(0)
    @field:Max(32)
    @field:NotNull
    val mask: Int,

    @field:NotBlank
    val areaName: String

)