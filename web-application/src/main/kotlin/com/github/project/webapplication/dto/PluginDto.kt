package com.github.project.webapplication.dto

data class PluginDto(

    val name: String,
    val description: String,
    val author: String

)

data class LoadPluginsDto(val filenames: List<String>)
