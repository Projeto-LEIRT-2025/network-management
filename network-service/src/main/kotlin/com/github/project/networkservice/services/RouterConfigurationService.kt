package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.response.InterfaceMac
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.exceptions.ManyRouterLoginException
import com.github.project.networkservice.exceptions.PluginNotFoundException
import com.github.project.networkservice.exceptions.RouterConfigurationException
import com.github.project.networkservice.exceptions.RouterLoginException
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.models.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

@Service
class RouterConfigurationService(

    private val routerService: RouterService,
    private val pluginLoader: PluginLoader

) {

    /**
     *
     * Enable an interface on the router given its name
     *
     * @param routerId router id
     * @param username router username
     * @param password router password
     * @param interfaceName interface name to enable
     *
     */
    fun enableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.enableInterface(interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    /**
     *
     * Disable an interface on the router given its name
     *
     * @param routerId router id
     * @param username router username
     * @param password router password
     * @param interfaceName interface name to disable
     *
     */
    fun disableInterface(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.disableInterface(interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun setIpAddress(
        routerId: Long, username: String, password: String, interfaceName: String, ipAddress: String, prefix: Int
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.setIpAddress(interfaceName, ipAddress, prefix)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun removeIpAddress(routerId: Long, username: String, password: String, interfaceName: String) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.removeIpAddress(interfaceName)

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

    fun addStaticRoute(
        routerId: Long, username: String, password: String, gateway: String, ipAddress: String, prefix: Int
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addStaticRoute(gateway, ipAddress, prefix)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun removeStaticRoute(routerId: Long, username: String, password: String, vararg identifiers: Int) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.removeStaticRoute(*identifiers)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createOSPFProcess(
        routerId: Long, username: String, password: String, processId: String, theRouterId: String
    ) {

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

    fun addOSPFNetwork(
        routerId: Long, username: String, password: String, network: String, prefix: Int, areaName: String
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addOSPFNetworks(network, prefix, areaName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun addOSPFInterface(
        routerId: Long, username: String, password: String, interfaceName: String, areaName: String,
        networkType: String, cost: Int
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.addOSPFInterface(interfaceName, areaName, networkType, cost)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createAddressPool(
        routerId: Long, username: String, password: String, name: String, rangeStart: String, rangeEnd: String
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createAddressPool(name, rangeStart, rangeEnd)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServer(
        routerId: Long, username: String, password: String, name: String, poolName: String, interfaceName: String
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServer(name, poolName, interfaceName)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServerRelay(
        routerId: Long, username: String, password: String, name: String, poolName: String,
        interfaceName: String, relayAddress: String
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServerRelay(name, poolName, interfaceName, relayAddress)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPServerNetwork(
        routerId: Long, username: String, password: String, network: String, prefix: Int, gateway: String
    ) {

        val routerConfiguration = getRouterConfigurationByRouterId(routerId, username, password)
        val response = routerConfiguration.createDHCPServerNetwork(network, prefix, gateway)

        if (response.raw.isNotBlank())
            throw RouterConfigurationException(response.raw)

    }

    fun createDHCPRelay(
        routerId: Long, username: String, password: String, name: String, interfaceName: String, serverAddress: String
    ) {

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

    @Suppress("UNCHECKED_CAST")
    suspend fun getNetworkGraph(
        allCredentials: Map<Long, CredentialsDto>,
        parallel: Int = 10
    ): Graph = coroutineScope {

        val graph = Graph()
        val routers = routerService.getAll()
        val routerConfigurations = ConcurrentHashMap<Router, RouterConfiguration>()
        val missingCredentials = Collections.synchronizedList(mutableListOf<Long>())

        routers.chunked(parallel).forEach { batch ->

            val configurationJobs = batch.mapNotNull { router ->

                val credentials = allCredentials[router.id]
                if (credentials == null) {
                    missingCredentials.add(router.id)
                    return@mapNotNull null
                }

                async(Dispatchers.IO) {
                    try {
                        router to router.toRouterConfiguration(
                            username = credentials.username,
                            password = credentials.password
                        )
                    } catch (_: RouterLoginException) {
                        missingCredentials.add(router.id)
                        null
                    }
                }
            }

            val batchConfigurationResults = configurationJobs.awaitAll().filterNotNull()

            batchConfigurationResults.forEach { (router, config) ->
                routerConfigurations[router] = config
            }
        }

        if (missingCredentials.isNotEmpty()) {
            throw ManyRouterLoginException(missingCredentials)
        }

        val routerInterfaces = routerConfigurations.mapValues { it.value.getInterfacesMac().data }
        val routerNeighbors = routerConfigurations.mapValues { it.value.getNeighbors().data }

        for ((router, neighbors) in routerNeighbors) {
            val source = Node(router.id.toString(), "${router.model}@${router.ipAddress}")

            for (neighbor in neighbors) {

                val routerNeighbor = routerInterfaces.entries.firstOrNull { (_, interfaces) ->
                    interfaces.any { it.mac.equals(neighbor.mac, ignoreCase = true) }
                }?.key ?: continue

                val target = Node(
                    id = routerNeighbor.id.toString(),
                    label = "${routerNeighbor.model}@${routerNeighbor.ipAddress}"
                )

                graph.addNode(source)
                graph.addNode(target)
                graph.addEdge(
                    sourceId = source.id,
                    targetId = target.id,
                    sourceInterface = neighbor.connectedInterface,
                    targetInterface = neighbor.interfaceName
                )
            }
        }

        return@coroutineScope graph
    }

    private fun getRouterConfigurationByRouterId(
        id: Long, username: String, password: String
    ): RouterConfiguration {

        val router = routerService.getById(id)
        
        return router.toRouterConfiguration(
            username = username,
            password = password
        )
    }

    private fun Router.toRouterConfiguration(
        username: String, password: String, port: Int = 23
    ): RouterConfiguration {

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