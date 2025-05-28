package com.github.project.networkservice.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class RemoveIpAddressDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @field:NotEmpty
    val identifiers: List<Int>

)
