package com.github.project.metricsservice.models

import com.github.project.api.router.response.NetworkInterface
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "interface_stats")
data class InterfaceStats(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val routerId: Long = 0,

    val interfaceName: String = "",

    val bytesIn: Long = 0,

    val bytesOut: Long = 0,

    val packetsIn: Long = 0,

    val packetsOut: Long = 0,

    val errorPacketsIn: Long = 0,

    val errorPacketsOut: Long = 0,

    val discardedPacketsIn: Long = 0,

    val discardedPacketsOut: Long = 0,

    @Enumerated(EnumType.STRING)
    val status: NetworkInterface.OperationalStatus = NetworkInterface.OperationalStatus.UNKNOWN,

    @CreationTimestamp
    val timestamp: Instant = Instant.now()

)