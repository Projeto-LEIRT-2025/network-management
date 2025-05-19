package com.github.project.metricsservice.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceStatsDto(

    @SerialName("router_id")
    val routerId: Long,

    val uptime: String,

    @SerialName("cpu_usage")
    val cpuUsage: Double,

    @SerialName("total_memory")
    val totalMemory: Int,

    @SerialName("memory_usage")
    val memoryUsage: Int,

    @SerialName("free_memory")
    val freeMemory: Int,

    val timestamp: String

)