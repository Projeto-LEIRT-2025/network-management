package com.github.project.networkservice.dto

import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateAddressPoolDto(

    val credentials: CredentialsDto,

    @field:NotBlank
    val name: String,

    @IpAddress
    val address: String,

    @field:Min(0)
    @field:Max(32)
    @field:NotNull
    val mask: Int

)
