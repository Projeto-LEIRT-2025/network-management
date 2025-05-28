package com.github.project.networkservice.exceptions

class RouterNotFoundException(

    override val message: String = "The router was not found"

) : RuntimeException(message)