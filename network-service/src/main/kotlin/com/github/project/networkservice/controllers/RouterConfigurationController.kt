package com.github.project.networkservice.controllers

import com.github.project.networkservice.dto.*
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

        routerConfigurationService.setIpAddress(id, dto.credentials.username, dto.credentials.password, dto.interfaceName, dto.ipAddress)

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

    @PostMapping("/snmp/version")
    fun changeSNMPVersion(@PathVariable id: Long, @RequestBody @Valid dto: ChangeSNMPVersionDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.changeSNMPVersion(id, dto.credentials.username, dto.credentials.password, dto.version)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP version changed successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/ospf/process")
    fun createOSPFProcess(@PathVariable id: Long, @RequestBody @Valid dto: CreateOSPFProcessDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createOSPFProcess(id, dto.credentials.username, dto.credentials.password, dto.processId, dto.routerId)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF process created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/ospf/network")
    fun addOSPFNetwork(@PathVariable id: Long, @RequestBody @Valid dto: AddOSPFNetworkDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addOSPFNetwork(id, dto.credentials.username, dto.credentials.password, dto.network, dto.mask, dto.areaName)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF network added successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/ospf/interface")
    fun addOSPFInterface(@PathVariable id: Long, @RequestBody @Valid dto: AddOSPFInterfaceDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addOSPFInterface(id, dto.credentials.username, dto.credentials.password, dto.interfaceName, dto.areaName, dto.networkType, dto.cost)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF interface added successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("/route/static")
    fun addStaticRoute(@PathVariable id: Long, @RequestBody @Valid dto: StaticRouteDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addStaticRoute(id, dto.credentials.username, dto.credentials.password, dto.gateway, dto.ipAddress, dto.mask)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Static route added successfully",
                    data = Unit
                )
            )
    }

}