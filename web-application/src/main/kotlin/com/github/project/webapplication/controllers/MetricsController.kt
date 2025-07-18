package com.github.project.webapplication.controllers

import com.github.project.api.router.response.NetworkInterface
import com.github.project.metricsservice.dto.DeviceStatsDto
import com.github.project.metricsservice.dto.InterfaceStatsDto
import com.github.project.metricsservice.models.DeviceStats
import com.github.project.metricsservice.models.InterfaceStats
import com.github.project.metricsservice.services.MetricsService
import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.NetworkInterfaceDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}/{id}\${METRICS_BASE_PATH}")
class MetricsController(

    private val metricsService: MetricsService

) {

    @Operation(summary = "Get interfaces from the router")
    @ApiResponse(responseCode = "200", description = "Interfaces retrieved successfully")
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
    @GetMapping("\${METRICS_INTERFACES_PATH}")
    fun getInterfaces(@PathVariable id: Long): ResponseEntity<ApiResponseDto<List<NetworkInterfaceDto>>> {
        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interfaces retrieved successfully",
                    data = metricsService.getNetworkInterfaces(id).map { it.toDto() }
                )
            )
    }

    @Operation(summary = "Get interface status from the router")
    @ApiResponse(responseCode = "200", description = "Interface status retrieved successfully")
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
        description = "Interface not found",
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
    @GetMapping("\${METRICS_INTERFACES_PATH}/{name}")
    fun getInterfaceStatus(
        @PathVariable id: Long, @PathVariable name: String
    ): ResponseEntity<ApiResponseDto<NetworkInterface.OperationalStatus>> {
        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface status retrieved successfully",
                    data = metricsService.getNetworkInterfaceStatus(id, name)
                )
            )
    }

    @Operation(summary = "Get interfaces stats from the router")
    @ApiResponse(responseCode = "200", description = "Interface stats retrieved successfully")
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @GetMapping("\${METRICS_INTERFACES_STATS_PATH}")
    fun getInterfaceStats(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant
    ): ResponseEntity<ApiResponseDto<List<InterfaceStatsDto>>> {
        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Interface stats retrieved successfully",
                    data = metricsService.getInterfaceStatsBetween(
                        routerId = id,
                        fromTimestamp = start,
                        toTimestamp = end
                    ).map { it.toDto() }
                )
            )
    }

    @Operation(summary = "Get device stats from the router")
    @ApiResponse(responseCode = "200", description = "Device stats retrieved successfully")
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @GetMapping("\${METRICS_DEVICE_STATS_PATH}")
    fun getDeviceStats(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant
    ): ResponseEntity<ApiResponseDto<List<DeviceStatsDto>>> {
        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Device stats retrieved successfully",
                    data = metricsService.getDeviceStatsBetween(
                        routerId = id,
                        fromTimestamp = start,
                        toTimestamp = end
                    ).map { it.toDto() }
                )
            )
    }

    private fun InterfaceStats.toDto() = InterfaceStatsDto(
        routerId = this.routerId,
        interfaceName = this.interfaceName,
        bytesIn = this.bytesIn,
        bytesOut = this.bytesOut,
        packetsIn = this.packetsIn,
        packetsOut = this.packetsOut,
        errorPacketsIn = this.errorPacketsIn,
        errorPacketsOut = this.errorPacketsOut,
        discardedPacketsIn = this.discardedPacketsIn,
        discardedPacketsOut = this.discardedPacketsOut,
        status = this.status.toString(),
        timestamp = this.timestamp.toString()
    )

    private fun DeviceStats.toDto() = DeviceStatsDto(
        routerId = this.routerId,
        uptime = this.uptime,
        cpuUsage = this.cpuUsage,
        totalMemory = this.totalMemory,
        memoryUsage = this.memoryUsage,
        freeMemory = this.freeMemory,
        timestamp = this.timestamp.toString()
    )

    private fun NetworkInterface.toDto() = NetworkInterfaceDto(
        this.name,
        this.operationalStatus
    )

}