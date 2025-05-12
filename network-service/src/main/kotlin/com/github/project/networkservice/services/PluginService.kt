package com.github.project.networkservice.services

import com.github.project.api.Plugin
import com.github.project.api.PluginLoader
import com.github.project.networkservice.exceptions.PluginLoadException
import com.github.project.networkservice.exceptions.PluginNotFoundException
import org.springframework.stereotype.Service

@Service
class PluginService {

    /**
     *
     * Get all plugins loaded
     *
     */

    fun listPlugins(): List<Plugin> {
        return PluginLoader.listPlugins()
    }

    /**
     *
     * Get plugin by the name
     *
     * @param pluginName plugin name
     *
     * @return the plugin
     *
     * @throws PluginNotFoundException if the plugin was not found
     *
     */

    fun getPlugin(pluginName: String): Plugin {
        return PluginLoader.getPlugin(pluginName) ?: throw PluginNotFoundException()
    }

    /**
     *
     * Disable all plugins given the plugins name
     *
     * @param pluginName the array of plugins names
     *
     * @throws PluginNotFoundException if the plugin was not found
     *
     */

    fun disablePlugin(vararg pluginName: String) {

        pluginName.forEach {

            val plugin = getPlugin(it)

            PluginLoader.disablePlugin(plugin)
        }

    }

    /**
     *
     * Enable plugins from the file names
     *
     * @param filenames the array of file names
     *
     * @return the list of plugins
     *
     * @throws PluginLoadException if an error occurred when load the plugins
     *
     */

    fun enablePlugins(vararg filenames: String): List<Plugin> {
        return try {
            PluginLoader.loadPlugins(*filenames)
        }catch (e: Exception) {
            throw PluginLoadException()
        }
    }

}