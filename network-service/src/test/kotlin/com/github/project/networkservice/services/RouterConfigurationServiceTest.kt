package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.response.Response
import com.github.project.networkservice.exceptions.RouterConfigurationException
import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Router
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class RouterConfigurationServiceTest {

    @Mock
    private lateinit var routerService: RouterService

    @Mock
    private lateinit var pluginLoader: PluginLoader

    @Mock
    private lateinit var routerConfiguration: RouterConfiguration

    @InjectMocks
    private lateinit var routerConfigurationService: RouterConfigurationService

    @Test
    fun `enable interface from router that does not exist should fail`() {

        val username = "admin"
        val password = "admin"
        val interfaceName = "eth1"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenThrow(RouterNotFoundException::class.java)

        assertThrows<RouterNotFoundException> {
            routerConfigurationService.enableInterface(router.id, username, password, interfaceName)
        }
    }

    @Test
    fun `enable interface should succeed`() {

        val username = "admin"
        val password = "admin"
        val interfaceName = "eth1"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.enableInterface(interfaceName))
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.enableInterface(router.id, username, password, interfaceName)

        verify(routerConfiguration).enableInterface(interfaceName)
    }

    @Test
    fun `set an ip address should succeed`() {

        val username = "admin"
        val password = "admin"
        val interfaceName = "eth1"
        val ipAddress = "192.168.0.3"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.setIpAddress(interfaceName, ipAddress))
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.setIpAddress(router.id, username, password, interfaceName, ipAddress)

        verify(routerConfiguration).setIpAddress(interfaceName, ipAddress)
    }

    @Test
    fun `enable SNMP should succeed`() {

        val username = "admin"
        val password = "admin"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.enableSNMP())
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.enableSNMP(router.id, username, password)

        verify(routerConfiguration).enableSNMP()
    }

    @Test
    fun `change SNMP version to a correct version should succeed`() {

        val username = "admin"
        val password = "admin"
        val version = 3
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.changeSNMPVersion(version))
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.changeSNMPVersion(router.id, username, password, version)

        verify(routerConfiguration).changeSNMPVersion(version)
    }

    @Test
    fun `change SNMP version to an incorrect version should fail`() {

        val username = "admin"
        val password = "admin"
        val version = 3
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.changeSNMPVersion(version))
            .thenReturn(Response(raw = "Invalid SNMP version", data = Unit))

        val exception = assertThrows<RouterConfigurationException> {
            routerConfigurationService.changeSNMPVersion(router.id, username, password, version)
        }

        assertEquals("Invalid SNMP version", exception.message)
        verify(routerConfiguration).changeSNMPVersion(version)

    }

    @Test
    fun `disable SNMP should succeed`() {

        val username = "admin"
        val password = "admin"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.disableSNMP())
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.disableSNMP(router.id, username, password)

        verify(routerConfiguration).disableSNMP()
    }

    @Test
    fun `create OSPF process should succeed`() {

        val username = "admin"
        val password = "admin"
        val processId = "1"
        val theRouterId = "1.1.1.1"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.createOSPFProcess(processId, theRouterId))
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.createOSPFProcess(router.id, username, password, processId, theRouterId)

        verify(routerConfiguration).createOSPFProcess(processId, theRouterId)
    }

    @Test
    fun `create OSPF area with wrong processId shouldn't succeed`() {

        val username = "admin"
        val password = "admin"
        val processId = "-"
        val theAreaId = "0.0.0.1"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.createOSPFArea(theAreaId, processId))
            .thenReturn(Response(raw = "Invalid Process Id", data = Unit))

        val exception = assertThrows<RouterConfigurationException> {
            routerConfigurationService.createOSPFArea(router.id, username, password, theAreaId, processId)
        }

        assertEquals("Invalid Process Id", exception.message)
        verify(routerConfiguration).createOSPFArea(theAreaId, processId)
    }

    @Test
    fun `create DHCP server should succeed`() {

        val username = "admin"
        val password = "admin"
        val name = "pepeDHCP"
        val poolName = "pepePool"
        val interfaceName = "eth3"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.createDHCPServer(name, poolName, interfaceName))
            .thenReturn(Response(raw = "", data = Unit))

        routerConfigurationService.createDHCPServer(router.id, username, password, name, poolName, interfaceName)

        verify(routerConfiguration).createDHCPServer(name, poolName, interfaceName)
    }

    @Test
    fun `remove DHCP relay server that does not exist shouldn't succeed`() {

        val username = "admin"
        val password = "admin"
        val name = "pepeDHCPRelayServer"
        val router = Router(id = 1, vendor = "Mikrotik", ipAddress = "192.168.0.2", model = "Router OS")

        `when`(routerService.getById(1)).thenReturn(router)
        `when`(
            pluginLoader.getRouterConfiguration(
                model = router.model.lowercase(),
                hostname = router.ipAddress,
                username = username,
                password = password,
                port = 23
            )
        ).thenReturn(routerConfiguration)
        `when`(routerConfiguration.removeDHCPRelay(name))
            .thenReturn(Response(raw = "Invalid DHCP relay server name", data = Unit))

        val exception = assertThrows<RouterConfigurationException> {
            routerConfigurationService.removeDHCPRelay(router.id, username, password, name)
        }

        assertEquals("Invalid DHCP relay server name", exception.message)
        verify(routerConfiguration).removeDHCPRelay(name)
    }


}