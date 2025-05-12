package com.github.project.metricsservice.services

import com.github.project.metricsservice.models.DeviceStats
import com.github.project.metricsservice.models.InterfaceStats
import com.github.project.metricsservice.repositories.DeviceStatsRepository
import com.github.project.metricsservice.repositories.InterfaceStatsRepository
import com.github.project.networkservice.services.RouterMonitoringService
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class MetricsService(

    private val interfaceStatsRepository: InterfaceStatsRepository,
    private val deviceStatsRepository: DeviceStatsRepository,
    private val routerMonitoringService: RouterMonitoringService

) {

    fun getInterfaceStatsBetween(fromTimestamp: Instant, toTimestamp: Instant): List<InterfaceStats> {
        return interfaceStatsRepository.findByTimestampBetween(fromTimestamp, toTimestamp)
    }

    fun collectDeviceStats(routerId: Long) {

        val cpuUsage = routerMonitoringService.getCpuUsage(routerId)
        val memoryUsage = routerMonitoringService.getMemoryUsage(routerId)
        val totalMemory = routerMonitoringService.getTotalMemory(routerId)
        val uptime = routerMonitoringService.getUptime(routerId)

        deviceStatsRepository.save(
            DeviceStats(
                routerId = routerId,
                uptime = uptime,
                memoryUsage = memoryUsage,
                totalMemory = totalMemory,
                cpuUsage = cpuUsage
            )
        )

    }

    fun collectInterfaceStats(routerId: Long) {

        val networkInterfaces =  routerMonitoringService.getNetworkInterfaces(routerId)
        val bytesIn = networkInterfaces.associateWith { routerMonitoringService.getBytesIn(routerId, it.index) }
        val bytesOut = networkInterfaces.associateWith { routerMonitoringService.getBytesOut(routerId, it.index) }
        val packetsIn = networkInterfaces.associateWith { routerMonitoringService.getPacketsIn(routerId, it.index) }
        val packetsOut = networkInterfaces.associateWith { routerMonitoringService.getPacketsOut(routerId, it.index) }
        val errorPacketsIn = networkInterfaces.associateWith { routerMonitoringService.getErrorPacketsIn(routerId, it.index) }
        val errorPacketsOut = networkInterfaces.associateWith { routerMonitoringService.getErrorPacketsOut(routerId, it.index) }
        val discardedPacketsIn = networkInterfaces.associateWith { routerMonitoringService.getDiscardedPacketsIn(routerId, it.index) }
        val discardedPacketsOut = networkInterfaces.associateWith { routerMonitoringService.getDiscardedPacketsOut(routerId, it.index) }

        networkInterfaces.map { networkInterface ->
            interfaceStatsRepository.save(
                InterfaceStats(
                    routerId = routerId,
                    interfaceName = networkInterface.name,
                    bytesIn = bytesIn[networkInterface] ?: 0L,
                    bytesOut = bytesOut[networkInterface] ?: 0L,
                    packetsIn = packetsIn[networkInterface] ?: 0L,
                    packetsOut = packetsOut[networkInterface] ?: 0L,
                    errorPacketsIn = errorPacketsIn[networkInterface] ?: 0L,
                    errorPacketsOut = errorPacketsOut[networkInterface] ?: 0L,
                    discardedPacketsIn = discardedPacketsIn[networkInterface] ?: 0L,
                    discardedPacketsOut = discardedPacketsOut[networkInterface] ?: 0L
                )
            )
        }
    }

}