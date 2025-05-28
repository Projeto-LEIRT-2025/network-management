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

    fun getMemoryUsage(routerId: Long): Int {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getMemoryUsage()
    }

    fun getUptime(routerId: Long): String {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getUptime()
    }

    fun getCpuUsage(routerId: Long): Double {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getCpuUsage()
    }

    fun getBytesIn(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getBytesIn(index)
    }

    fun getBytesOut(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getBytesOut(index)
    }

    fun getPacketsIn(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getPacketsIn(index)
    }

    fun getPacketsOut(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getPacketsOut(index)
    }

    fun getErrorPacketsIn(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getErrorPacketsIn(index)
    }

    fun getErrorPacketsOut(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getErrorPacketsOut(index)
    }

    fun getDiscardedPacketsIn(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getDiscardedPacketsIn(index)
    }

    fun getDiscardedPacketsOut(routerId: Long, index: Int): Long {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getDiscardedPacketsOut(index)
    }

    fun getNetworkInterfaces(routerId: Long): List<NetworkInterface> {

        val routerMonitoring = getRouterMonitoringByRouterId(routerId)

        return routerMonitoring.getNetworkInterfaces()
    }

    private fun getRouterMonitoringByRouterId(id: Long): RouterMonitoring {

        val router = routerService.getById(id)

        return router.toRouterMonitoring()
    }

    private fun Router.toRouterMonitoring(port: Int = 161) =
        pluginLoader.getRouterMonitoring(
            model = model.lowercase(),
            hostname = ipAddress,
            port = port
        ) ?: throw PluginNotFoundException("There is no plugin for this device")

}