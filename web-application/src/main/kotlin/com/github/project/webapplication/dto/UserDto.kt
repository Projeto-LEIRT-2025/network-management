package com.github.project.webapplication.dto

import jakarta.validation.constraints.NotBlank

data class UserDto(

    val username: String,
    val token: String

)

data class LoginDto(

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String

)
