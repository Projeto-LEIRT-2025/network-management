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
    lateinit var name: String
    lateinit var description: String
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

    fun getRouterConfiguration(model: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration? {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerConfigurationManager.get(model) } ?: return null

        return createInstance(theClass.name, hostname, port.toString(), username, password)
    }

    fun getRouterMonitoring(model: String, hostname: String, port: Int): RouterMonitoring? {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerMonitoringManager.get(model) } ?: return null

        return createInstance(theClass.name, hostname, port.toString())
    }

    fun listPlugins(): List<Plugin> =
        this.plugins.toList()

    fun getPlugin(name: String) =
        this.plugins.firstOrNull { it.metadata.name == name }

    fun disablePlugin(plugin: Plugin) =
        this.plugins.remove(plugin)

    fun loadPlugins(vararg filesNames: String): List<Plugin> {
        return filesNames
            .map { File(it) }
            .map { loadPlugin(it) }
    }

    fun loadPluginsFromDirectory(directoryName: String): List<Plugin> {

        val directory = File(directoryName)

        if (!directory.isDirectory)
            throw IllegalArgumentException("$directory is not a directory")

        return directory.listFiles()!!
            .filter { file -> file.extension == "jar" }
            .map { file -> loadPlugin(file) }
    }

    private fun loadPlugin(file: File): Plugin {

        if (file.isDirectory)
            throw IllegalArgumentException("$file is a directory")

        if (file.extension != "jar")
            throw IllegalArgumentException("$file is not a JAR")

        addFilesToClassLoader(file)
        return loadPluginFromJarFile(JarFile(file))
    }

    private fun addFilesToClassLoader(vararg files: File) {

        val urls = files.map { it.toURI().toURL() }

        if (this::classLoader.isInitialized) {
            this.classLoader = URLClassLoader(urls.toTypedArray(), this.classLoader)
            return
        }

        this.classLoader = URLClassLoader.newInstance(urls.toTypedArray(), this.javaClass.classLoader)
    }

    private fun loadPluginFromJarFile(jarFile: JarFile): Plugin {

        val entry = jarFile.getJarEntry("plugin.yaml") ?: throw IllegalStateException("Plugin ${jarFile.name} has an invalid plugin.yaml")
        val inputStream = jarFile.getInputStream(entry)
        val yaml = Yaml()
        val pluginMetadata = yaml.loadAs(inputStream, PluginMetadata::class.java)
        val className = pluginMetadata.entryPoint
        val plugin = createInstance<Plugin>(className, pluginMetadata)

        Logger.getGlobal().log(Level.INFO, "Loading `${pluginMetadata.name}` plugin developed by {0}", pluginMetadata.author);
        plugin.initialize()
        plugins.add(plugin)

        jarFile.close()
        return plugin
    }

}