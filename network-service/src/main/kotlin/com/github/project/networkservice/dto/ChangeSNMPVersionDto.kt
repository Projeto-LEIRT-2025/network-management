package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotNull

data class ChangeSNMPVersionDto(

    val credentials: CredentialsDto,

    @field:NotNull
    val version: Int

)
