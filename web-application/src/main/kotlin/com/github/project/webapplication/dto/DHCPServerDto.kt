package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateDHCPServerRelayDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @SerialName("pool_name")
    @field:NotBlank
    val poolName: String,

    @SerialName("interface_name")
    @field:NotBlank
    val interfaceName: String,

    @SerialName("relay_address")
    @IpAddress
    val relayAddress: String

)

data class CreateDHCPServerNetworkDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @IpAddress
    val network: String,

    @field:NotNull
    @field:Min(0)
    @field:Max(32)
    val mask: Int,

    @field:NotBlank
    val gateway: String

)

@Serializable
data class CreateDHCPServerDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @SerialName("pool_name")
    @field:NotBlank
    val poolName: String,

    @SerialName("interface_name")
    @field:NotBlank
    val interfaceName: String

)