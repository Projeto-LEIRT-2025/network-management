package com.github.project.api

import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.RouterMonitoring
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile
import java.util.logging.Level
import java.util.logging.Logger

class PluginMetadata {
    lateinit var author: String
    lateinit var entryPoint: String
}

object PluginLoader {

    private val plugins = mutableListOf<Plugin>()
    private lateinit var classLoader: ClassLoader

    private inline fun <reified T> createInstance(className: String, vararg params: Any): T {

        checkClassLoaderIsInitialized()

        val theClass = Class.forName(className, true, classLoader)
        val constructor = theClass.getConstructor(*params.map { it.javaClass }.toTypedArray())
        val instance = constructor.newInstance(*params)

        if (instance !is T)
            throw IllegalArgumentException("Class $className is not a $instance")

        return instance
    }

    private fun checkClassLoaderIsInitialized() {

        if (!::classLoader.isInitialized)
            throw IllegalStateException("ClassLoader must be initialized before classLoader is called")

    }

    /*
    fun loadRouterConfiguration(className: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration =
        createInstance(className, hostname, port.toString(), username, password)

    fun loadRouterMonitoring(className: String, hostname: String, port: Int): RouterMonitoring =
        createInstance(className, hostname, port.toString())
     */

    fun getRouterConfiguration(model: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerConfigurationManager.get(model) } ?: throw IllegalArgumentException("$model does not exist")

        return createInstance(theClass.name, hostname, port.toString(), username, password)
    }

    fun getRouterMonitoring(model: String, hostname: String, port: Int): RouterMonitoring {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerMonitoringManager.get(model) } ?: throw IllegalArgumentException("$model does not exist")

        return createInstance(theClass.name, hostname, port.toString())
    }

    fun loadPlugins(directoryName: String) {

        val directory = File(directoryName)

        if (!directory.isDirectory)
            throw IllegalArgumentException("Directory $directory is not a directory")

        val files = directory.listFiles()
            ?.filter { file -> file.extension == "jar" } ?: emptyList()

        this.classLoader = URLClassLoader.newInstance(files.map { it.toURI().toURL() }.toTypedArray(), this::class.java.classLoader)

        val jarFiles = files.map { JarFile(it) }

        for (jarFile in jarFiles) {

            val entry = jarFile.getJarEntry("plugin.yaml") ?: throw IllegalStateException("Plugin ${jarFile.name} has an invalid plugin.yaml")
            val inputStream = jarFile.getInputStream(entry)
            val yaml = Yaml()
            val pluginMetadata = yaml.loadAs(inputStream, PluginMetadata::class.java)
            val className = pluginMetadata.entryPoint
            val plugin = createInstance<Plugin>(className)

            Logger.getGlobal().log(Level.INFO, "Loading plugin developed by {0}", pluginMetadata.author);
            plugin.initialize()
            plugins.add(plugin)

            inputStream.close()
        }

    }

}