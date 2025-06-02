package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.response.InterfaceIpAddress
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.exceptions.PluginNotFoundException
import com.github.project.networkservice.exceptions.RouterConfigurationException
import com.github.project.networkservice.exceptions.RouterLoginException
import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.models.Router
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class RouterConfigurationService(

    private val routerService: RouterService,
    private val pluginLoader: PluginLoader

) {

    fun enableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.enableInterface(interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun disableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.enableInterface(interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun setIpAddress(routerId: Long, username: String, password: String, interfaceName: String, ipAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.setIpAddress(interfaceName, ipAddress)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun removeIpAddress(routerId: Long, username: String, password: String, vararg identifiers: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.removeIpAddress(*identifiers)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun enableSNMP(routerId: Long, username: String, password: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.enableSNMP()

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun disableSNMP(routerId: Long, username: String, password: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.disableSNMP()

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun changeSNMPVersion(routerId: Long, username: String, password: String, version: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.changeSNMPVersion(version)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun addStaticRoute(routerId: Long, username: String, password: String, gateway: String, ipAddress: String, mask: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addStaticRoute(gateway, ipAddress, mask)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun removeStaticRoute(routerId: Long, username: String, password: String, vararg identifiers: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.removeStaticRoute(*identifiers)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createOSPFProcess(routerId: Long, username: String, password: String, processId: String, theRouterId: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createOSPFProcess(processId, theRouterId)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createOSPFArea(routerId: Long, username: String, password: String, areaId: String, processId: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createOSPFArea(areaId, processId)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun addOSPFNetwork(routerId: Long, username: String, password: String, network: String, mask: Int, areaName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addOSPFNetworks(network, mask, areaName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun addOSPFInterface(routerId: Long, username: String, password: String, interfaceName: String, areaName: String, networkType: String, cost: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addOSPFInterface(interfaceName, areaName, networkType, cost)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createAddressPool(routerId: Long, username: String, password: String, name: String, rangeStart: String, rangeEnd: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createAddressPool(name, rangeStart, rangeEnd)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServer(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServer(name, poolName, interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServerRelay(routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String, relayAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServerRelay(name, poolName, interfaceName, relayAddress)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServerNetwork(routerId: Long, username: String, password: String, network: String, mask: Int, gateway: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServerNetwork(network, mask, gateway)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPRelay(routerId: Long, username: String, password: String, name: String, interfaceName: String, serverAddress: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPRelay(name, interfaceName, serverAddress)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }
    
    fun enableDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.enableDHCPRelay(name)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun disableDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.disableDHCPRelay(name)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun removeDHCPRelay(routerId: Long, username: String, password: String, name: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.removeDHCPRelay(name)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun getIpAddresses(routerId: Long, username: String, password: String): List<InterfaceIpAddress> {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.getIpAddresses()

        return response.data
    }

    fun getNetworkGraph(allCredentials: Map<Long, CredentialsDto>, parallel: Int = 10): Graph {

        val graph = Graph()
        val routers = routerService.getAll()
        val routerConfigurations = mutableMapOf<Router, RouterConfiguration>()

        routers.chunked(parallel).forEach { batch ->

            val futures = batch.associateWith { router ->

                val credentials = allCredentials[router.id] ?: throw RouterLoginException(router.id)

                CompletableFuture.supplyAsync {
                    router.toRouterConfiguration(
                        username = credentials.username,
                        password = credentials.password
                    )
                }

            }

            CompletableFuture.allOf(*futures.values.toTypedArray()).join()

            futures.forEach { (router, future) ->
                routerConfigurations[router] = future.get()
            }

        }

        val routerInterfaces = routerConfigurations.mapValues { it.value.getIpAddresses().data }
        val routerNeighbors = routerConfigurations.mapValues { it.value.getNeighbors().data }

        for ((router, neighbors) in routerNeighbors) {

            val source = Node(router.id.toString(), "${router.model}@${router.ipAddress}")

            for (neighbor in neighbors) {

                val routerNeighbor = routerInterfaces.entries.firstOrNull { (_, interfaces) ->
                    interfaces.any {
                        it.name == neighbor.interfaceName && it.address == neighbor.ipAddress
                    }
                }?.key ?: continue

                val target = Node(routerNeighbor.id.toString(), "${routerNeighbor.model}@${routerNeighbor.ipAddress}")

                graph.addNode(source)
                graph.addNode(target)
                graph.addEdge(source.id, target.id)
            }

        }

        return graph
    }

    private fun getRouterConfigurationByRouterId(id: Long, username: String, password: String): RouterConfiguration {

        val router = routerService.getById(id)
        
        return router.toRouterConfiguration(
            username = username,
            password = password
        )
    }

    private fun Router.toRouterConfiguration(username: String, password: String, port: Int = 23): RouterConfiguration {

        val routerConfiguration = pluginLoader.getRouterConfiguration(
            model = model.lowercase(),
            hostname = ipAddress,
            username = username,
            password = password,
            port = port
        ) ?: throw PluginNotFoundException("There is no plugin for this device")

        if (!routerConfiguration.login())
            throw RouterLoginException(this.id)

        return routerConfiguration
    }

}