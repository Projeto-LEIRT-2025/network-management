package com.github.project.api.manager

import com.github.project.api.router.RouterConfiguration

/*
 *
 * All plugins have one class of this type and the purpose is
 * to register or retrieve the implementations of RouterConfiguration
 *
 */

class RouterConfigurationManager {

    private val routerConfigurations = mutableMapOf<String, Class<out RouterConfiguration>>()

    /**
     *
     * Register RouterConfiguration to a specific model of device
     *
     * @param model model of the device
     * @param configuration class to instantiate the RouterConfiguration
     *
     */

    fun register(model: String, configuration: Class<out RouterConfiguration>) {
        routerConfigurations[model] = configuration
    }

    /**
     * Get RouterConfiguration class to instantiate
     *
     * @param model model of the device
     *
     * @return RouterConfiguration class
     *
     */

    fun get(model: String): Class<out RouterConfiguration>? {
        return routerConfigurations[model]
    }

}