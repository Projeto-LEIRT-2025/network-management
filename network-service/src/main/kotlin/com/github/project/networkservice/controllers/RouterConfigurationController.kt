package com.github.project.networkservice.controllers

import com.github.project.networkservice.dto.SetIpAddressDto
import com.github.project.networkservice.services.RouterConfigurationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routers/configuration")
class RouterConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @PostMapping
    fun setIpAddress(@RequestBody @Valid dto: SetIpAddressDto): ResponseEntity<Unit> {

        routerConfigurationService.setIpAddress(dto.routerId, dto.username, dto.password, dto.interfaceName, dto.ipAddress)

        return ResponseEntity
            .noContent()
            .build()
    }

}