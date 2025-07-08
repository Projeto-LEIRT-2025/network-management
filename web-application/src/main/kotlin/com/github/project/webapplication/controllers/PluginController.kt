package com.github.project.webapplication.controllers

import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.PluginDto
import com.github.project.networkservice.services.PluginService
import com.github.project.webapplication.dto.toDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("\${PLUGINS_BASE_PATH}")
class PluginController(

    private val pluginService: PluginService

) {

    @Operation(summary = "List Plugins")
    @ApiResponse(
        responseCode = "200",
        description = "Plugins listed successfully"
    )
    @GetMapping
    fun listPlugins(): ResponseEntity<ApiResponseDto<List<PluginDto>>> {

        val plugins = pluginService.listPlugins()

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugins retrieved successfully",
                    data = plugins.map { it.key.toDto(it.value) }
                )
            )
    }

    @Operation(summary = "Get Plugin", description = "get a certain plugin which the name is sent as variable")
    @ApiResponse(
        responseCode = "200",
        description = "Plugin retrieved successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @GetMapping("/{name}")
    fun getPlugin(@PathVariable name: String): ResponseEntity<ApiResponseDto<PluginDto>> {

        val pair = pluginService.getPlugin(name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin retrieved successfully",
                    data = pair.first.toDto(pair.second)
                )
            )
    }

    @Operation(summary = "Disable Plugin", description = "disable a certain plugin which the name is sent as variable")
    @ApiResponse(
        responseCode = "200",
        description = "Plugin disabled successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("/{name}\${PLUGINS_DISABLE_PATH}")
    fun disablePlugin(@PathVariable name: String): ResponseEntity<ApiResponseDto<Unit>> {

        pluginService.disablePlugin(name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin disabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Enable Plugin", description = "enable a certain plugin which the name is sent as variable")
    @ApiResponse(
        responseCode = "200",
        description = "Plugin enabled successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("/{name}\${PLUGINS_ENABLE_PATH}")
    fun enablePlugin(@PathVariable name: String): ResponseEntity<ApiResponseDto<Unit>> {

        pluginService.enablePlugin(name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin enabled successfully",
                    data = Unit
                )
            )
    }

    @Operation(summary = "Upload Plugin", description = "upload a certain plugin which the plugin is sent as variable")
    @ApiResponse(
        responseCode = "200",
        description = "Plugin uploaded successfully"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Plugin already exists",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @PostMapping("\${PLUGINS_UPLOAD_PATH}")
    fun uploadPlugin(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ApiResponseDto<PluginDto>> {

        val plugin = pluginService.uploadPlugin(
            fileName = file.originalFilename ?: UUID.randomUUID().toString(),
            content = file.bytes
        )

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin uploaded successfully",
                    data = plugin.toDto(false)
                )
            )
    }

    @Operation(summary = "Delete Plugin", description = "delete a certain plugin which the name is sent as variable")
    @ApiResponse(
        responseCode = "200",
        description = "Plugin deleted successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plugin does not exist",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @DeleteMapping("/{name}")
    fun deletePlugin(@PathVariable name: String): ResponseEntity<ApiResponseDto<Unit>> {

        pluginService.deletePlugin(name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin deleted successfully",
                    data = Unit
                )
            )
    }

}