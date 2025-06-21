package com.github.project.api

import com.github.project.api.manager.RouterConfigurationManager
import com.github.project.api.manager.RouterMonitoringManager

/**
 *
 * All the plugins should inherit from this class.
 * It's the entry point of the plugin.
 *
 */

abstract class Plugin(val metadata: PluginMetadata) {

    val routerConfigurationManager = RouterConfigurationManager()
    val routerMonitoringManager = RouterMonitoringManager()

    /**
     *
     * This function is called when the plugin is enabled.
     * The developer should register all the implementations of
     * RouterConfiguration and RouterMonitoring using the respective managers
     *
     */

    abstract fun enable()

    /**
     *
     * This function is called when the plugin is disabled.
     * The developer should unregister all the implementations of
     * RouterConfiguration and RouterMonitoring using the respective managers
     *
     */

    abstract fun disable()

}