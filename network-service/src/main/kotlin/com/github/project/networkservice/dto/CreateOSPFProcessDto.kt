package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
