package com.github.project.networkservice.exceptions

class PluginAlreadyExistsException(

    override val message: String = "Already exists a plugin with this name or file name"

) : RuntimeException(message)