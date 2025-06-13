package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class RemoveStaticRouteDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotEmpty
    val identifiers: List<Int>

)
