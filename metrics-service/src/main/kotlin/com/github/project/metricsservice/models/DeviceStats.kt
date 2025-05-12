package com.github.project.metricsservice.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "device_stats")
data class DeviceStats(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val routerId: Long,

    val uptime: String,

    val cpuUsage: Double,

    val totalMemory: Int,

    val memoryUsage: Int,

    @CreationTimestamp
    val timestamp: Instant = Instant.now()

) {

    val freeMemory: Int
        get() = memoryUsage - totalMemory

}