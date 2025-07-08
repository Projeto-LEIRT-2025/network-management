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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}/{id}\${CONFIGURATION_BASE_PATH}")
class RouterConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @Operation(summary = "Set IP Address in the router interface")
    @ApiResponse(responseCode = "200", description = "Successfully set ip address")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_ADDRESS_PATH}")
    fun setIpAddress(
        @PathVariable id: Long, @RequestBody @Valid dto: SetIpAddressDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.setIpAddress(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            interfaceName = dto.interfaceName,
            ipAddress = dto.ipAddress,
            mask = dto.mask
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Successfully set ip address",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Remove IP Address from the router interface")
    @ApiResponse(responseCode = "200", description = "Successfully remove ip address")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @DeleteMapping("\${CONFIGURATION_ADDRESS_PATH}")
    fun removeIpAddress(
        @PathVariable id: Long, @RequestBody @Valid dto: RemoveIpAddressDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.removeIpAddress(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            interfaceName = dto.interfaceName
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Successfully remove ip address",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Enable router interface")
    @ApiResponse(responseCode = "200", description = "Interface enabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_INTERFACES_PATH}/{name}\${CONFIGURATION_INTERFACES_ENABLE_PATH}")
    fun enableInterface(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableInterface(
            routerId = id,
            username = dto.username,
            password = dto.password,
            interfaceName = name
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface enabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Disable router interface")
    @ApiResponse(responseCode = "200", description = "Interface disabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_INTERFACES_PATH}/{name}\${CONFIGURATION_INTERFACES_DISABLE_PATH}")
    fun disableInterface(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableInterface(
            routerId = id,
            username = dto.username,
            password = dto.password,
            interfaceName = name
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface disabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Enable router SNMP")
    @ApiResponse(responseCode = "200", description = "SNMP enabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_SNMP_ENABLE_PATH}")
    fun enableSNMP(
        @PathVariable id: Long, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableSNMP(id, dto.username, dto.password)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP enabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Disable router SNMP")
    @ApiResponse(responseCode = "200", description = "SNMP disabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_SNMP_DISABLE_PATH}")
    fun disableSNMP(
        @PathVariable id: Long, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableSNMP(id, dto.username, dto.password)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP disabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Change router SNMP version")
    @ApiResponse(responseCode = "200", description = "SNMP version changed successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_SNMP_VERSION_PATH}")
    fun changeSNMPVersion(
        @PathVariable id: Long, @RequestBody @Valid dto: ChangeSNMPVersionDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.changeSNMPVersion(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            version = dto.version
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "SNMP version changed successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create OSPF process")
    @ApiResponse(responseCode = "200", description = "OSPF processed created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_OSPF_PROCESS_PATH}")
    fun createOSPFProcess(
        @PathVariable id: Long, @RequestBody @Valid dto: CreateOSPFProcessDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createOSPFProcess(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            processId = dto.processId,
            theRouterId = dto.routerId
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF process created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create OSPF area")
    @ApiResponse(responseCode = "200", description = "OSPF area created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_OSPF_AREA_PATH}")
    fun createOSPFArea(
        @PathVariable id: Long, @RequestBody @Valid dto: CreateOSPFAreaDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createOSPFArea(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            areaId = dto.areaId,
            processId = dto.processId
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF area created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Add OSPF network")
    @ApiResponse(responseCode = "200", description = "OSPF network added successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_OSPF_NETWORK_PATH}")
    fun addOSPFNetwork(
        @PathVariable id: Long, @RequestBody @Valid dto: AddOSPFNetworkDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addOSPFNetwork(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            network = dto.network,
            mask = dto.mask,
            areaName = dto.areaName
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF network added successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Add OSPF interface")
    @ApiResponse(responseCode = "200", description = "OSPF interface added successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_OSPF_INTERFACE_PATH}")
    fun addOSPFInterface(
        @PathVariable id: Long, @RequestBody @Valid dto: AddOSPFInterfaceDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addOSPFInterface(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            interfaceName = dto.interfaceName,
            areaName = dto.areaName,
            networkType = dto.networkType,
            cost = dto.cost
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "OSPF interface added successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Add static route")
    @ApiResponse(responseCode = "200", description = "Static route added successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_ROUTE_STATIC_PATH}")
    fun addStaticRoute(
        @PathVariable id: Long, @RequestBody @Valid dto: StaticRouteDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.addStaticRoute(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            gateway = dto.gateway,
            ipAddress = dto.ipAddress,
            mask = dto.mask
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Static route added successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Remove static route")
    @ApiResponse(responseCode = "200", description = "Static route removed successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @DeleteMapping("\${CONFIGURATION_ROUTE_STATIC_PATH}")
    fun removeStaticRoute(
        @PathVariable id: Long, @RequestBody @Valid dto: RemoveStaticRouteDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.removeStaticRoute(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            identifiers = dto.identifiers.toIntArray()
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Static route removed successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create address pool")
    @ApiResponse(responseCode = "200", description = "Address pool created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_ADDRESS_POOL_PATH}/{name}")
    fun createAddressPool(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateAddressPoolDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createAddressPool(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            name = name,
            rangeStart = dto.rangeStart,
            rangeEnd = dto.rangeEnd
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Address pool created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create DHCP Server")
    @ApiResponse(responseCode = "200", description = "DHCP Server created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_SERVER_PATH}/{name}")
    fun createDHCPServer(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPServerDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServer(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            name = name,
            poolName = dto.poolName,
            interfaceName = dto.interfaceName
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create DHCP Server Relay")
    @ApiResponse(responseCode = "200", description = "DHCP server relay created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_SERVER_RELAY_PATH}/{name}")
    fun createDHCPServerRelay(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPServerRelayDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServerRelay(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            name = name,
            poolName = dto.poolName,
            interfaceName = dto.interfaceName,
            relayAddress = dto.relayAddress
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server relay created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create DHCP Server network")
    @ApiResponse(responseCode = "200", description = "DHCP Server network created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_SERVER_NETWORK_PATH}")
    fun createDHCPServerNetwork(
        @PathVariable id: Long, @RequestBody @Valid dto: CreateDHCPServerNetworkDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPServerNetwork(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            network = dto.network,
            mask = dto.mask,
            gateway = dto.gateway
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP server network created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Create DHCP Relay")
    @ApiResponse(responseCode = "200", description = "DHCP Relay created successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}")
    fun createDHCPRelay(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CreateDHCPRelayDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.createDHCPRelay(
            routerId = id,
            username = dto.credentials.username,
            password = dto.credentials.password,
            name = name,
            interfaceName = dto.interfaceName,
            serverAddress = dto.serverAddress
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay created successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Enable DHCP Relay")
    @ApiResponse(responseCode = "200", description = "DHCP relay enabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}\${CONFIGURATION_DHCP_RELAY_ENABLE_PATH}")
    fun enableDHCPRelay(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.enableDHCPRelay(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay enabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Disable DHCP Relay")
    @ApiResponse(responseCode = "200", description = "DHCP relay disabled successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}\${CONFIGURATION_DHCP_RELAY_DISABLE_PATH}")
    fun disableDHCPRelay(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

        routerConfigurationService.disableDHCPRelay(id, dto.username, dto.password, name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "DHCP relay disabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Remove DHCP Relay")
    @ApiResponse(responseCode = "200", description = "DHCP Relay removed successfully")
    @ApiResponse(
        responseCode = "400",
        description = "Something went wrong with router configuration",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "401",
        description = "Router credentials are incorrect",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @DeleteMapping("\${CONFIGURATION_DHCP_RELAY_PATH}/{name}")
    fun removeDHCPRelay(
        @PathVariable id: Long, @PathVariable name: String, @RequestBody @Valid dto: CredentialsDto
    ): ResponseEntity<ApiResponseDto<Unit>> {

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