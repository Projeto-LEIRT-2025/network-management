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

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.enableInterface(interfaceName)
    }

    fun disableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.enableInterface(interfaceName)
    }

    fun setIpAddress(routerId: Long, username: String, password: String, interfaceName: String, ipAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.setIpAddress(interfaceName, ipAddress)
    }

    fun removeIpAddress(routerId: Long, username: String, password: String, vararg identifiers: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.removeIpAddress(*identifiers)
    }

    fun enableSNMP(routerId: Long, username: String, password: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.enableSNMP()
    }

    fun disableSNMP(routerId: Long, username: String, password: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.disableSNMP()
    }

    fun changeSNMPVersion(routerId: Long, username: String, password: String, version: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.changeSNMPVersion(version)
    }

    fun addStaticRoute(routerId: Long, username: String, password: String, gateway: String, ipAddress: String, mask: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.addStaticRoute(gateway, ipAddress, mask)
    }

    fun removeStaticRoute(routerId: Long, username: String, password: String, vararg identifiers: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.removeStaticRoute(*identifiers)
    }

    fun createOSPFProcess(routerId: Long, username: String, password: String, processId: String, theRouterId: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createOSPFProcess(processId, theRouterId)
    }

    fun createOSPFArea(routerId: Long, username: String, password: String, areaId: String, processId: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createOSPFArea(areaId, processId)
    }

    fun addOSPFNetwork(routerId: Long, username: String, password: String, network: String, mask: Int, areaName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.addOSPFNetworks(network, mask, areaName)
    }

    fun addOSPFInterface(routerId: Long, username: String, password: String, interfaceName: String, areaName: String, networkType: String, cost: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.addOSPFInterface(interfaceName, areaName, networkType, cost)
    }

    fun createAddressPool(routerId: Long, username: String, password: String, name: String, address: String, mask: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createAddressPool(name, address, mask)
    }

    fun createDHCPServer(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createDHCPServer(name, poolName, interfaceName)
    }

    fun createDHCPServerRelay(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String, relayAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createDHCPServerRelay(name, poolName, interfaceName, relayAddress)
    }

    fun createDHCPServerNetwork(routerId: Long, username: String, password: String, network: String, mask: Int, gateway: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createDHCPServerNetwork(network, mask, gateway)
    }

    fun createDHCPRelay(routerId: Long, username: String, password: String, name: String, interfaceName: String, serverAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.createDHCPRelay(name, interfaceName, serverAddress)
    }
    
    fun enableDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        
        routerConfiguration.enableDHCPRelay(name)
    }

    fun disableDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

        routerConfiguration.disableDHCPRelay(name)
    }

    fun removeDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)

       routerConfiguration.removeDHCPRelay(name)
    }

    private fun getRouterConfigurationByRouterId(id: Long, username: String, password: String): RouterConfiguration {

        val router = routerService.getById(id)
        
        return router.toRouterConfiguration(
            username = username,
            password = password
        )
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