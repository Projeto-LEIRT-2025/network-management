package com.github.project.networkservice.controllers

import com.github.project.networkservice.dto.ApiResponseDto
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.dto.SetIpAddressDto
import com.github.project.networkservice.services.RouterConfigurationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routers/{id}/configuration")
class RouterConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @PostMapping("/address")
    fun setIpAddress(@PathVariable id: Long, @RequestBody @Valid dto: SetIpAddressDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.setIpAddress(id, dto.username, dto.password, dto.interfaceName, dto.ipAddress)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Successfully set ip address",
                    data = Unit
                )
            )
    }

    @PostMapping("/interfaces/{name}/enable")
    fun enableInterface(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableInterface(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface enabled successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/interfaces/{name}/disable")
    fun disableInterface(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableInterface(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface disabled successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/snmp/enable")
    fun enableSNMP(@PathVariable id: Long, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableSNMP(id, dto.username, dto.password)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP enabled successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/snmp/disable")
    fun disableSNMP(@PathVariable id: Long, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableSNMP(id, dto.username, dto.password)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP disabled successfully",
                    data = Unit
                )
            )
    }

}