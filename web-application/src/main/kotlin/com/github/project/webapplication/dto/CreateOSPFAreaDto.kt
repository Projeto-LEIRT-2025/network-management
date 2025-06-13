package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateOSPFAreaDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotBlank
    val areaId: String,

    @field:NotBlank
    val processId: String

)
