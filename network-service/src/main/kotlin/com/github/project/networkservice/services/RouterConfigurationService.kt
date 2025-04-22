package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterConfiguration
import com.github.project.networkservice.exceptions.PluginNotFoundException
import com.github.project.networkservice.models.Router
import org.springframework.stereotype.Service

@Service
class RouterConfigurationService(

    private val routerService: RouterService,
    private val pluginLoader: PluginLoader

) {

    fun enableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.enableInterface(interfaceName)
    }

    fun disableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.enableInterface(interfaceName)
    }

    fun setIpAddress(routerId: Long, username: String, password: String, interfaceName: String, ipAddress: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.setIpAddress(interfaceName, ipAddress)
    }

    fun enableSNMP(routerId: Long, username: String, password: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.enableSNMP()
    }

    fun disableSNMP(routerId: Long, username: String, password: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.disableSNMP()
    }

    fun changeSNMPVersion(routerId: Long, username: String, password: String, version: Int) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.changeSNMPVersion(version)
    }

    fun addStaticRoute(routerId: Long, username: String, password: String, gateway: String, ipAddress: String, mask: Int) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.addStaticRoute(gateway, ipAddress, mask)
    }

    fun createOSPFProcess(routerId: Long, username: String, password: String, processId: String, theRouterId: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.createOSPFProcess(processId, theRouterId)
    }

    fun addOSPFNetwork(routerId: Long, username: String, password: String, network: String, mask: Int, areaName: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.addOSPFNetworks(network, mask, areaName)
    }

    fun addOSPFInterface(routerId: Long, username: String, password: String, interfaceName: String, areaName: String, networkType: String, cost: Int) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.addOSPFInterface(interfaceName, areaName, networkType, cost)
    }

    fun createAddressPool(routerId: Long, username: String, password: String, name: String, address: String, mask: Int) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.createAddressPool(name, address, mask)
    }

    fun createDHCPServer(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.createDHCPServer(name, poolName, interfaceName)
    }

    fun createDHCPServerRelay(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String, relayAddress: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.createDHCPServerRelay(name, poolName, interfaceName, relayAddress)
    }

    private fun Router.toRouterConfiguration(username: String, password: String, port: Int = 23) =
        pluginLoader.getRouterConfiguration(
            model = model.lowercase(),
            hostname = ipAddress,
            username = username,
            password = password,
            port = port
        ) ?: throw PluginNotFoundException("There is no plugin for this device")

}