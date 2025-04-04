package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import org.springframework.stereotype.Service

@Service
class RouterConfigurationService(

    private val routerService: RouterService

) {

    fun setIpAddress(routerId: Long, username: String, password: String, interfaceName: String, ipAddress: String) {

        val router = routerService.getById(routerId)
        val routerConfiguration = PluginLoader.getRouterConfiguration(
            model = router.model.lowercase(),
            hostname = router.ipAddress,
            username = username,
            password = password,
            port = 23
        )

        routerConfiguration.setIpAddress(interfaceName, ipAddress)
    }

}