package com.github.project.networkservice.exceptions

class PluginNotFoundException(

    override val message: String = "Plugin not found"

) : RuntimeException(message)