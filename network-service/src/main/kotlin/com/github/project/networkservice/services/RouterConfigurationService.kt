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

    fun addStaticRoute(routerId: Long, username: String, password: String, gateway: String, ipAddress: String, mask: Int) {

        val router = routerService.getById(routerId)
        val routerConfiguration = router.toRouterConfiguration(
            username = username,
            password = password
        )

        routerConfiguration.addStaticRoute(gateway, ipAddress, mask)
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