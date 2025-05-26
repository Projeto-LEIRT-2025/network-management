package com.github.project.networkservice.dto

import com.github.project.networkservice.models.Node

data class GraphDto(

    val nodes: List<Node>,
    val edges: List<EdgeDto>

)

data class EdgeDto(

    val source: String,
    val target: String

)
