package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateDHCPRelayDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @SerialName("interface_name")
    @field:NotBlank
    val interfaceName: String,

    @SerialName("server_address")
    @IpAddress
    val serverAddress: String

)
