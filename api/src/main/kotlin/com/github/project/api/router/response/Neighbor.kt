package com.github.project.api.router.response

data class Neighbor(

    val connectedInterface: String,

    val interfaceName: String,

    val ipAddress: String,

    val mac: String

)