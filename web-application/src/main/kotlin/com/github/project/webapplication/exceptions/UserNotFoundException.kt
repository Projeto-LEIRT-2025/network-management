package com.github.project.webapplication.exceptions

class UserNotFoundException(

    override val message: String = "User not found"

) : RuntimeException(message)