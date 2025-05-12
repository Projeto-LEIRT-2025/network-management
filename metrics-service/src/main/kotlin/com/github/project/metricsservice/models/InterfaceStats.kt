package com.github.project.metricsservice.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "interface_stats")
data class InterfaceStats(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val routerId: Long,

    val interfaceName: String,

    val bytesIn: Long,

    val bytesOut: Long,

    val packetsIn: Long,

    val packetsOut: Long,

    val errorPacketsIn: Long,

    val errorPacketsOut: Long,

    val discardedPacketsIn: Long,

    val discardedPacketsOut: Long,

    @CreationTimestamp
    val timestamp: Instant = Instant.now()

)