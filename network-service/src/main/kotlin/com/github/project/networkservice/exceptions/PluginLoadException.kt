package com.github.project.networkservice.exceptions

class PluginLoadException(

    override val message: String = "An error occurred while loading plugins"

) : RuntimeException(message)