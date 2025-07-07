package com.github.project.webapplication.controllers

import com.github.project.api.router.response.NetworkInterface
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.networkservice.services.RouterMonitoringService
import com.github.project.webapplication.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}\${CONFIGURATION_BASE_PATH}")
class RouterGlobalConfigurationController(

    private val routerConfigurationService: RouterConfigurationService,
    private val routerMonitoringService: RouterMonitoringService

) {

    @Operation(summary = "Get Network Topology")
    @ApiResponse(
        responseCode = "200",
        description = "Network Topology retrieved successfully"
    )
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
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${ROUTERS_NETWORK_PATH}")
    fun getNetwork(
        @RequestBody @Valid dto: Map<Long, CredentialsDto>
    ): ResponseEntity<ApiResponseDto<GraphDto>> = runBlocking {

        val graph = routerConfigurationService.getNetworkGraph(dto)

        ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Graph retrieved successfully",
                    data = GraphDto(
                        nodes = graph.nodes.map { NodeDto(it.id, it.label) },
                        edges = graph.edges.map {

                            val sourceInterface = routerMonitoringService.getNetworkInterfaces(it.source.id.toLong())
                                .firstOrNull { networkInterface -> networkInterface.name == it.sourceInterface }

                            val targetInterface = routerMonitoringService.getNetworkInterfaces(it.target.id.toLong())
                                .firstOrNull { networkInterface -> networkInterface.name == it.targetInterface }

                            EdgeDto(
                                source = it.source.id,
                                target = it.target.id,
                                sourceInterface = sourceInterface?.toDto() ?: NetworkInterfaceDto(it.sourceInterface),
                                targetInterface = targetInterface?.toDto() ?: NetworkInterfaceDto(it.targetInterface)
                            )
                        }
                    )
                )
            )
    }

    private fun NetworkInterface.toDto() = NetworkInterfaceDto(
        this.name,
        this.operationalStatus
    )

}