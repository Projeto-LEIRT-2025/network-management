package com.github.project.networkservice.exceptions

import java.lang.RuntimeException

class RouterLoginException(

    val routerId: Long,
    override val message: String = "An error occurred while logging in to the router"

) : RuntimeException(message)

class ManyRouterLoginException(

    val routersId: List<Long>,
    override val message: String = "An error occurred while logging in to the routers"

) : RuntimeException(message)