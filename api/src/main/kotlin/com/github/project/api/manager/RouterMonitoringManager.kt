package com.github.project.api.manager

import com.github.project.api.router.RouterMonitoring

class RouterMonitoringManager {

    private val routerMonitoring = mutableMapOf<String, Class<out RouterMonitoring>>()

    fun register(model: String, monitoring: Class<out RouterMonitoring>) {
        routerMonitoring[model] = monitoring
    }

    fun get(model: String): Class<out RouterMonitoring>? {
        return routerMonitoring[model]
    }

}