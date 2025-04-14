package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(

    @field:NotBlank
    val username: String = "",

    @field:NotBlank
    val password: String = ""

)
