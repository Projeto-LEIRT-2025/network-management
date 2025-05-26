package com.github.project.networkservice.exceptions

class RouterIpAddressAlreadyExistsException(

    override val message: String = "The router ip address already exists"

) : RuntimeException(message)
