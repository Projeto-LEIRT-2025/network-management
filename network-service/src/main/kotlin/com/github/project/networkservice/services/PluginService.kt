package com.github.project.networkservice.services

import com.github.project.api.Plugin
import com.github.project.api.PluginLoader
import com.github.project.networkservice.exceptions.PluginAlreadyExistsException
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

    fun listPlugins(): Map<Plugin, Boolean> {
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

    fun getPlugin(pluginName: String): Pair<Plugin, Boolean> {
        return PluginLoader.getPlugin(pluginName) ?: throw PluginNotFoundException()
    }

    /**
     *
     * Disable all plugins given the names
     *
     * @param pluginName the array of plugins names
     *
     * @throws PluginNotFoundException if the plugin was not found
     *
     */

    fun disablePlugin(vararg pluginName: String) {

        pluginName.forEach {

            val plugin = getPlugin(it).first

            PluginLoader.disablePlugin(plugin)
        }

    }

    /**
     *
     * Enable all plugins given the names
     *
     * @param pluginName the array of plugins names
     *
     * @throws PluginNotFoundException if the plugin was not found
     *
     */

    fun enablePlugin(vararg pluginName: String) {

        pluginName.forEach {

            val plugin = getPlugin(it).first

            PluginLoader.enablePlugin(plugin)
        }

    }

    /**
     *
     * Uploads a plugin JAR file from a byte array and loads it into the system.
     *
     * @param fileName the name of the plugin file (must end with .jar)
     * @param content the byte array content of the plugin
     *
     * @return the loaded plugin
     *
     * @throws PluginLoadException if loading fails
     *
     */
    fun uploadPlugin(fileName: String, content: ByteArray): Plugin {
        return PluginLoader.uploadPlugin(fileName, content) ?: throw PluginAlreadyExistsException()
    }

    /**
     * Deletes a plugin by its name.
     *
     * @param name The name of the plugin to be deleted.
     * @throws PluginLoadException If the plugin could not be deleted successfully.
     */
    fun deletePlugin(name: String) {

        val pair = getPlugin(name)

        if (pair.second)
            disablePlugin(name)

        if (!PluginLoader.deletePlugin(pair.first))
            throw PluginLoadException("Failed to delete plugin: ${pair.first.metadata.name}")

    }

}