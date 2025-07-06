package com.github.project.webapplication.dto

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.webapplication.validators.IpAddress
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
