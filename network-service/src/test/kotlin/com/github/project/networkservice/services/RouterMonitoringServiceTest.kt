package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterMonitoring
import com.github.project.api.router.response.NetworkInterface
import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Router
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RouterMonitoringServiceTest {

    @Mock
    private lateinit var routerService: RouterService

    @Mock
    private lateinit var pluginLoader: PluginLoader

    @Mock
    private lateinit var routerMonitoring: RouterMonitoring

    @InjectMocks
    private lateinit var routerMonitoringService: RouterMonitoringService

    @Test
    fun `get total memory should succeed`() {

        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterMonitoring(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                port = 161
            )
        ).thenReturn(routerMonitoring)
        `when`(routerMonitoring.getTotalMemory())
            .thenReturn(1000)

        routerMonitoringService.getTotalMemory(router.id)

        verify(routerMonitoring).getTotalMemory()
    }

    @Test
    fun `get up time from a router that does not exist shouldn't succeed`() {

        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenThrow(RouterNotFoundException::class.java)

        assertThrows<RouterNotFoundException> {
            routerMonitoringService.getUptime(router.id)
        }
    }

    @Test
    fun `get network interfaces should succeed`() {

        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterMonitoring(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                port = 161
            )
        ).thenReturn(routerMonitoring)
        `when`(routerMonitoring.getNetworkInterfaces())
            .thenReturn(listOf(NetworkInterface(1,"eth0", 1500, "0f-12-31-69", NetworkInterface.OperationalStatus.UP)))

        routerMonitoringService.getNetworkInterfaces(router.id)

        verify(routerMonitoring).getNetworkInterfaces()
    }


}