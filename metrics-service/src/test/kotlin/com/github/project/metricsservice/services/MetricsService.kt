package com.github.project.metricsservice.services

import com.github.project.metricsservice.repositories.DeviceStatsRepository
import com.github.project.metricsservice.repositories.InterfaceStatsRepository
import com.github.project.networkservice.services.RouterMonitoringService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class MetricsService {

    @Mock
    private lateinit var routerMonitoringService: RouterMonitoringService

    @Mock
    private lateinit var deviceStatsRepository: DeviceStatsRepository

    @Mock
    private lateinit var interfaceStatsRepository: InterfaceStatsRepository

    @InjectMocks
    private lateinit var metricsService: MetricsService

}