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

    val routerId: Long = 0,

    val uptime: String = "",

    val cpuUsage: Double = 0.0,

    val totalMemory: Int = 0,

    val memoryUsage: Int = 0,

    @CreationTimestamp
    val timestamp: Instant = Instant.now()

) {

    val freeMemory: Int
        get() = totalMemory - memoryUsage

}