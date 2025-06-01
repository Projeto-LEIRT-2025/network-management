package com.github.project.networkservice.dto

import com.github.project.networkservice.validators.IpAddress
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAddressPoolDto(

    @field:NotNull
    val credentials: CredentialsDto,

    @IpAddress
    @SerialName("range_start")
    val rangeStart: String,

    @IpAddress
    @SerialName("range_end")
    val rangeEnd: String

)
