package com.github.project.networkservice.exceptions

import java.lang.RuntimeException

class RouterLoginException(
    override val message: String = "An error occurred while logging in to the router"
) : RuntimeException(message)