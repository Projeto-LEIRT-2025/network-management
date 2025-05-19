package com.github.project.metricsservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InterfaceStatsDto(

    @SerialName("router_id")
    val routerId: Long,

    @SerialName("interface_name")
    val interfaceName: String,

    @SerialName("bytes_int")
    val bytesIn: Long,

    @SerialName("bytes_out")
    val bytesOut: Long,

    @SerialName("packets_in")
    val packetsIn: Long,

    @SerialName("packets_out")
    val packetsOut: Long,

    @SerialName("error_packets_in")
    val errorPacketsIn: Long,

    @SerialName("error_packets_out")
    val errorPacketsOut: Long,

    @SerialName("discarded_packets_in")
    val discardedPacketsIn: Long,

    @SerialName("discarded_packets_out")
    val discardedPacketsOut: Long,

    val status: String,

    val timestamp: String

)