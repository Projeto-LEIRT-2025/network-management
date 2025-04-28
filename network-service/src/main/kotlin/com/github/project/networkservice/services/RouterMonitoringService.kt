package com.github.project.networkservice.services

import com.github.project.api.PluginLoader
import com.github.project.api.router.RouterMonitoring
import com.github.project.api.router.response.NetworkInterface
import com.github.project.networkservice.exceptions.PluginNotFoundException
import com.github.project.networkservice.models.Router
import org.springframework.stereotype.Service

@Service
class RouterMonitoringService(

    private val pluginLoader: PluginLoader,
    private val routerService: RouterService

) {

    fun getTotalMemory(routerId: Long): Int {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getTotalMemory()
    }

    fun getMemoryUsed(routerId: Long): Int {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getMemoryUsed()
    }

    fun getUptime(routerId: Long): String {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getUptime()
    }

    fun getCpuLoad(routerId: Long): Double {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getCpuLoad()
    }

    fun getBytesIn(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getBytesIn(index)
    }

    fun getBytesOut(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getBytesOut(index)
    }

    fun getNetworkInterfaces(routerId: Long): List<NetworkInterface> {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getNetworkInterfaces()
    }

    private fun getRouterMonitoringByRouterId(id: Long): RouterMonitoring {

        val router = routerService.getById(id)

        return router.toRouterMonitoring()
    }

    private fun Router.toRouterMonitoring(port: Int = 23) =
        pluginLoader.getRouterMonitoring(
            model = model.lowercase(),
            hostname = ipAddress,
            port = port
        ) ?: throw PluginNotFoundException("There is no plugin for this device")

}