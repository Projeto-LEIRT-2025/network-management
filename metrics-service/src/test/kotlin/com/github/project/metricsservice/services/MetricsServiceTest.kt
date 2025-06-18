package com.github.project.metricsservice.services

import com.github.project.api.router.response.NetworkInterface
import com.github.project.metricsservice.models.InterfaceStats
import com.github.project.metricsservice.repositories.DeviceStatsRepository
import com.github.project.metricsservice.repositories.InterfaceStatsRepository
import com.github.project.networkservice.services.RouterMonitoringService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class MetricsServiceTest {

    @Mock
    private lateinit var routerMonitoringService: RouterMonitoringService

    @Mock
    private lateinit var deviceStatsRepository: DeviceStatsRepository

    @Mock
    private lateinit var interfaceStatsRepository: InterfaceStatsRepository

    @InjectMocks
    private lateinit var metricsService: MetricsService

    @Test
    fun `get interface stats between a period should succeed`() {

        val routerId = 1L
        val start = Instant.now()
        val end = start.plusSeconds(3600)
        val expected = listOf(
            InterfaceStats(
                id = 1,
                routerId = routerId,
                interfaceName = "ether1",
                status = NetworkInterface.OperationalStatus.UP,
                timestamp = start.plusSeconds(1800)
            )
        )

        `when`(
            interfaceStatsRepository.findByRouterIdAndTimestampBetween(
                routerId = 1,
                fromTimestamp = start,
                toTimestamp = end
            )
        ).thenReturn(expected)

        val actual = metricsService.getInterfaceStatsBetween(routerId, start, end)

        assertEquals(expected, actual)
    }

}