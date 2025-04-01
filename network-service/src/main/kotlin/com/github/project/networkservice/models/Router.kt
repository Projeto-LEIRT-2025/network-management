package com.github.project.networkservice.models

import jakarta.persistence.*

@Entity
@Table(name = "routers")
data class Router(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val vendor: String = "",

    val ipAddress: String = "",

    val model: String = ""

)