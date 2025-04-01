package com.github.project.networkservice.exceptions

class RouterNotFoundException(

    override val message: String = "Router not found"

) : RuntimeException(message)