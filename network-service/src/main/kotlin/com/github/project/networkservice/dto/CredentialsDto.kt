package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotBlank

data class CredentialsDto(

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String

)
