package com.github.project.api

import com.github.project.api.manager.RouterConfigurationManager
import com.github.project.api.manager.RouterMonitoringManager

abstract class Plugin {

    val routerConfigurationManager = RouterConfigurationManager()
    val routerMonitoringManager = RouterMonitoringManager()

    abstract fun initialize()

}