package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import jakarta.validation.constraints.NotNull

data class ChangeSNMPVersionDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotNull
    val version: Int

)
