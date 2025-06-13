package com.github.project.webapplication.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GraphDto(

    val nodes: List<NodeDto>,
    val edges: List<EdgeDto>

)

@Serializable
data class NodeDto(val id: String, val label: String)

@Serializable
data class EdgeDto(

    val source: String,
    val target: String,

    @SerialName("source_interface")
    val sourceInterface: NetworkInterfaceDto,

    @SerialName("target_interface")
    val targetInterface: NetworkInterfaceDto

)
