package com.github.project.networkservice.controllers

import com.github.project.api.Plugin
import com.github.project.networkservice.dto.ApiResponseDto
import com.github.project.networkservice.dto.LoadPluginsDto
import com.github.project.networkservice.dto.PluginDto
import com.github.project.networkservice.services.PluginService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/plugins")
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
                    data = plugins.map { it.toDto() }
                )
            )
    }

    @GetMapping("/{name}")
    fun getPlugin(@PathVariable name: String): ResponseEntity<ApiResponseDto<PluginDto>> {

        val plugin = pluginService.getPlugin(name)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugin retrieved successfully",
                    data = plugin.toDto()
                )
            )
    }

    @PostMapping("/{name}/disable")
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

    @PostMapping("/enable")
    fun enablePlugins(@RequestBody @Valid dto: LoadPluginsDto): ResponseEntity<ApiResponseDto<List<PluginDto>>> {

        val plugins = pluginService.enablePlugins(*dto.filenames.toTypedArray())

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Plugins enabled successfully",
                    data = plugins.map { it.toDto() }
                )
            )
    }

    private fun Plugin.toDto() = PluginDto(
        name = this.metadata.name,
        description = this.metadata.description,
        author = this.metadata.author
    )

}