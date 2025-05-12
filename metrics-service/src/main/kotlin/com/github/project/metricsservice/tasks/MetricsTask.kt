package com.github.project.metricsservice.tasks

import com.github.project.metricsservice.services.MetricsService
import com.github.project.networkservice.services.RouterService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MetricsTask(

    private val metricsService: MetricsService,
    private val routerService: RouterService

) {

    private val logger = LoggerFactory.getLogger(MetricsTask::class.java)

    @Scheduled(fixedRate = 30 * 1000L)
    fun reportInterfaceStats() {

        val routers = routerService.getAll()

        routers.forEach { router ->
            metricsService.collectInterfaceStats(router.id)
            metricsService.collectDeviceStats(router.id)
        }

        logger.info("Collected stats from ${routers.size} routers.")
    }

}