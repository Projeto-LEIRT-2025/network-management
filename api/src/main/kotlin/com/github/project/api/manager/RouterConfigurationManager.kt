package com.github.project.api.manager

import com.github.project.api.router.RouterConfiguration

class RouterConfigurationManager {

    private val routerConfigurations = mutableMapOf<String, Class<out RouterConfiguration>>()

    fun register(model: String, configuration: Class<out RouterConfiguration>) {
        routerConfigurations[model] = configuration
    }

    fun get(model: String): Class<out RouterConfiguration>? {
        return routerConfigurations[model]
    }

}