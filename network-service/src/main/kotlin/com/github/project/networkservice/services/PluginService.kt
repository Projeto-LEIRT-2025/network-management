package com.github.project.networkservice.services

import com.github.project.api.Plugin
import com.github.project.api.PluginLoader
import com.github.project.networkservice.exceptions.PluginLoadException
import com.github.project.networkservice.exceptions.PluginNotFoundException
import org.springframework.stereotype.Service

@Service
class PluginService {

    fun listPlugins(): List<Plugin> {
        return PluginLoader.listPlugins()
    }

    fun getPlugin(pluginName: String): Plugin {
        return PluginLoader.getPlugin(pluginName) ?: throw PluginNotFoundException()
    }

    fun disablePlugin(vararg pluginName: String) {

        pluginName.forEach {

            val plugin = getPlugin(it)

            PluginLoader.disablePlugin(plugin)
        }

    }

    fun enablePlugins(vararg filenames: String): List<Plugin> {
        return try {
            PluginLoader.loadPlugins(*filenames)
        }catch (e: Exception) {
            throw PluginLoadException()
        }
    }

}