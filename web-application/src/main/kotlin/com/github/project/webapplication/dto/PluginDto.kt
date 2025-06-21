package com.github.project.webapplication.dto

import com.github.project.api.Plugin

data class PluginDto(

    val name: String,
    val description: String,
    val author: String,
    val enabled: Boolean

)

fun Plugin.toDto(enabled: Boolean) = PluginDto(
    name = this.metadata.name,
    description = this.metadata.description,
    author = this.metadata.author,
    enabled = enabled
)
