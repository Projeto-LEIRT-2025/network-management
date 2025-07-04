package com.github.project.webapplication.controllers

import com.github.project.api.Plugin
import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.PluginDto
import com.github.project.networkservice.services.PluginService
import com.github.project.webapplication.dto.toDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("\${PLUGINS_BASE_PATH}")
class PluginController(

    private val pluginService: PluginService

) {

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

    @PostMapping("\${PLUGINS_UPLOAD_PATH}")
    fun uploadPlugin(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResponseDto<PluginDto>> {

        val plugin = pluginService.uploadPlugin(file.originalFilename ?: UUID.randomUUID().toString(), file.bytes)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin uploaded successfully",
                    data = plugin.toDto(false)
                )
            )
    }

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