package com.github.project.plugin.routeros

import com.github.project.api.Plugin
import com.github.project.api.PluginMetadata
import java.util.logging.Level
import java.util.logging.Logger

class RouterOSPlugin(metadata: PluginMetadata) : Plugin(metadata) {

    override fun initialize() {

        routerConfigurationManager.register("router-os", RouterConfigurationImpl::class.java)
        routerMonitoringManager.register("router-os", RouterMonitoringImpl::class.java)

        Logger.getGlobal().log(Level.INFO, "RouterOS plugin initialized")
    }

}