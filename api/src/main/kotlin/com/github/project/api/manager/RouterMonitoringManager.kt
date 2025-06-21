package com.github.project.api.manager

import com.github.project.api.router.RouterMonitoring

/**
 *
 * All plugins have one class of this type and the purpose is
 * to register or retrieve the implementations of RouterMonitoring
 *
 */

class RouterMonitoringManager {

    private val routerMonitoring = mutableMapOf<String, Class<out RouterMonitoring>>()

    /**
     *
     * Register RouterMonitoring to a specific model of device
     *
     * @param model model of the device
     * @param monitoring class to instantiate the RouterConfiguration
     *
     */

    fun register(model: String, monitoring: Class<out RouterMonitoring>) {
        routerMonitoring[model] = monitoring
    }

    /**
     * Get RouterMonitoring class to instantiate
     *
     * @param model model of the device
     *
     * @return RouterMonitoring class
     *
     */

    fun get(model: String): Class<out RouterMonitoring>? {
        return routerMonitoring[model]
    }

    /**
     *
     * Unregister RouterMonitoring to a specific model of device
     *
     * @param model model of the device
     *
     */

    fun unregister(model: String) {
        routerMonitoring.remove(model)
    }

}