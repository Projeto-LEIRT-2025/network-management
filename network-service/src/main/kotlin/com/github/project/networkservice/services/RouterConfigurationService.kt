package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.networkservice.models.Router
import org.springframework.stereotype.Service

@Service
class RouterConfigurationService(

    private val routerService: RouterService

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

    private fun Router.toRouterConfiguration(username: String, password: String, port: Int = 23) = PluginLoader.getRouterConfiguration(
        model = this.model.lowercase(),
        hostname = this.ipAddress,
        username = username,
        password = password,
        port = port
    )

}