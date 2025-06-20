package com.github.project.webapplication.controllers

import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.webapplication.dto.AddOSPFInterfaceDto
import com.github.project.webapplication.dto.AddOSPFNetworkDto
import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.ChangeSNMPVersionDto
import com.github.project.webapplication.dto.CreateAddressPoolDto
import com.github.project.webapplication.dto.CreateDHCPRelayDto
import com.github.project.webapplication.dto.CreateDHCPServerDto
import com.github.project.webapplication.dto.CreateDHCPServerNetworkDto
import com.github.project.webapplication.dto.CreateDHCPServerRelayDto
import com.github.project.webapplication.dto.CreateOSPFAreaDto
import com.github.project.webapplication.dto.CreateOSPFProcessDto
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.webapplication.dto.RemoveIpAddressDto
import com.github.project.webapplication.dto.RemoveStaticRouteDto
import com.github.project.webapplication.dto.SetIpAddressDto
import com.github.project.webapplication.dto.StaticRouteDto
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}/{id}\${CONFIGURATION_BASE_PATH}")
class RouterConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @PostMapping("\${CONFIGURATION_ADDRESS_PATH}")
    fun setIpAddress(@PathVariable id: Long, @RequestBody @Valid dto: SetIpAddressDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.setIpAddress(id, dto.credentials.username, dto.credentials.password, dto.interfaceName, dto.ipAddress, dto.mask)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Successfully set ip address",
                    data = Unit
                )
            )
    }

    @DeleteMapping("\${CONFIGURATION_ADDRESS_PATH}")
    fun removeIpAddress(@PathVariable id: Long, @RequestBody @Valid dto: RemoveIpAddressDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.removeIpAddress(id, dto.credentials.username, dto.credentials.password, *dto.identifiers.toIntArray())

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Successfully remove ip address",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_INTERFACES_PATH}/{name}\${CONFIGURATION_INTERFACES_ENABLE_PATH}")
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

    @PostMapping("\${CONFIGURATION_INTERFACES_PATH}/{name}\${CONFIGURATION_INTERFACES_DISABLE_PATH}")
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

    @PostMapping("\${CONFIGURATION_SNMP_ENABLE_PATH}")
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

    @PostMapping("\${CONFIGURATION_SNMP_DISABLE_PATH}")
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

    @PostMapping("\${CONFIGURATION_SNMP_VERSION_PATH}")
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

    @PostMapping("\${CONFIGURATION_OSPF_PROCESS_PATH}")
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

    @PostMapping("\${CONFIGURATION_OSPF_AREA_PATH}")
    fun createOSPFArea(@PathVariable id: Long, @RequestBody @Valid dto: CreateOSPFAreaDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createOSPFArea(id, dto.credentials.username, dto.credentials.password, dto.areaId, dto.processId)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF area created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_OSPF_NETWORK_PATH}")
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

    @PostMapping("\${CONFIGURATION_OSPF_INTERFACE_PATH}")
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

    @PostMapping("\${CONFIGURATION_ROUTE_STATIC_PATH}")
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

    @DeleteMapping("\${CONFIGURATION_ROUTE_STATIC_PATH}")
    fun removeStaticRoute(@PathVariable id: Long, @RequestBody @Valid dto: RemoveStaticRouteDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.removeStaticRoute(id, dto.credentials.username, dto.credentials.password, *dto.identifiers.toIntArray())

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Static route removed successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_ADDRESS_POOL_PATH}/{name}")
    fun createAddressPool(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateAddressPoolDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createAddressPool(id, dto.credentials.username, dto.credentials.password, name, dto.rangeStart, dto.rangeEnd)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Address pool created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_SERVER_PATH}/{name}")
    fun createDHCPServer(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPServerDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServer(id, dto.credentials.username, dto.credentials.password, name, dto.poolName, dto.interfaceName)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_SERVER_RELAY_PATH}/{name}")
    fun createDHCPServerRelay(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPServerRelayDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServerRelay(id, dto.credentials.username, dto.credentials.password, name, dto.poolName, dto.interfaceName, dto.relayAddress)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server relay created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_SERVER_NETWORK_PATH}")
    fun createDHCPServerNetwork(@PathVariable id: Long, @RequestBody @Valid dto: CreateDHCPServerNetworkDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServerNetwork(id, dto.credentials.username, dto.credentials.password, dto.network, dto.mask, dto.gateway)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server network created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}")
    fun createDHCPRelay(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPRelayDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPRelay(id, dto.credentials.username, dto.credentials.password, name, dto.interfaceName, dto.serverAddress)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay created successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}\${CONFIGURATION_DHCP_RELAY_ENABLE_PATH}")
    fun enableDHCPRelay(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableDHCPRelay(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay enabled successfully",
                    data = Unit
                )
            )
    }

    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}\${CONFIGURATION_DHCP_RELAY_DISABLE_PATH}")
    fun disableDHCPRelay(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableDHCPRelay(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay disabled successfully",
                    data = Unit
                )
            )
    }

    @DeleteMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}")
    fun removeDHCPRelay(@PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.removeDHCPRelay(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay removed successfully",
                    data = Unit
                )
            )
    }

}