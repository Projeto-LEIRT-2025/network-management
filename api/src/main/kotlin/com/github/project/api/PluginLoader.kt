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

/**
 *
 * This class is responsible to manage the plugins
 *
 */

object PluginLoader {

    private val plugins = mutableListOf<Plugin>()
    private var classLoader: ClassLoader = this.javaClass.classLoader

    /**
     *
     * Create and instance of T, given the class name and the parameters of constructor
     *
     * @param className the class name to instantiate
     * @param params all the parameters that should be passed to the constructor
     *
     * @return the type of the class name
     *
     * @throws IllegalArgumentException if the class does not exist
     *
     */

    private inline fun <reified T> createInstance(className: String, vararg params: Any): T {

        val theClass = Class.forName(className, true, classLoader)
        val constructor = theClass.getConstructor(*params.map { it.javaClass }.toTypedArray())
        val instance = constructor.newInstance(*params)

        if (instance !is T)
            throw IllegalArgumentException("Class $className is not a $instance")

        return instance
    }

    /**
     *
     * Instantiate a RouterConfiguration implementation.
     *
     * @param model model of device to retrieve the RouterConfiguration implementation
     * @param hostname hostname of device
     * @param port port of device
     * @param username username of device
     * @param password password of device
     *
     * @return the RouterConfiguration or null if the implementation does not exist
     *
     */

    fun getRouterConfiguration(model: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration? {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerConfigurationManager.get(model) } ?: return null

        return createInstance(theClass.name, hostname, port.toString(), username, password)
    }

    /**
     *
     * Instantiate a RouterMonitoring implementation.
     *
     * @param model model of device to retrieve the RouterConfiguration implementation
     * @param hostname hostname of device
     * @param port port of device
     *
     * @return the RouterMonitoring or null if the implementation does not exist
     *
     */

    fun getRouterMonitoring(model: String, hostname: String, port: Int): RouterMonitoring? {

        val theClass = this.plugins.firstNotNullOfOrNull { it.routerMonitoringManager.get(model) } ?: return null

        return createInstance(theClass.name, hostname, port.toString())
    }

    /**
     *
     * List the loaded plugins
     *
     * @return list of the plugins
     *
     */

    fun listPlugins(): List<Plugin> =
        this.plugins.toList()

    /**
     *
     * Get the plugin
     *
     * @param name name of the plugin
     *
     * @return the plugin or null if does not exist
     *
     */

    fun getPlugin(name: String) =
        this.plugins.firstOrNull { it.metadata.name == name }

    /**
     *
     * Disable the plugin
     *
     * @param plugin plugin to disable
     *
     */

    fun disablePlugin(plugin: Plugin) =
        this.plugins.remove(plugin)

    /**
     *
     * Load all plugins from the filenames
     *
     * @param filesNames the array of filenames
     *
     * @return list of loaded plugins
     *
     */

    fun loadPlugins(vararg filesNames: String): List<Plugin> {
        return filesNames
            .map { File(it) }
            .map { loadPlugin(it) }
    }

    /**
     *
     * Load all plugins from a directory
     *
     * @param directoryName the directory name
     *
     * @return list of loaded plugins
     *
     * @throws IllegalArgumentException if the directory name is not a directory
     *
     */

    fun loadPluginsFromDirectory(directoryName: String): List<Plugin> {

        val directory = File(directoryName)

        if (!directory.isDirectory)
            throw IllegalArgumentException("$directory is not a directory")

        return directory.listFiles()!!
            .filter { file -> file.extension == "jar" }
            .map { file -> loadPlugin(file) }
    }

    /**
     * Load a plugin given a file
     *
     * @param file the file
     *
     * @return the plugin
     *
     * @throws IllegalArgumentException if the file is a directory or is not a jar file
     *
     */

    private fun loadPlugin(file: File): Plugin {

        if (file.isDirectory)
            throw IllegalArgumentException("$file is a directory")

        if (file.extension != "jar")
            throw IllegalArgumentException("$file is not a JAR")

        addFilesToClassLoader(file)
        return loadPluginFromJarFile(JarFile(file))
    }

    /**
     *
     * Add files to class loader, then the system can use the classes
     *
     * @param files the files to add to the classpath
     *
     */

    private fun addFilesToClassLoader(vararg files: File) {

        val urls = files.map { it.toURI().toURL() }

        this.classLoader = URLClassLoader.newInstance(urls.toTypedArray(), this.javaClass.classLoader)
    }

    /**
     *
     * Load plugin from JarFile, then the system can get the implementations
     * of the plugin loaded. When is created an instance of Plugin, the method
     * initialize is called.
     *
     * @param jarFile the JarFile
     *
     * @return the loaded plugin
     *
     */

    private fun loadPluginFromJarFile(jarFile: JarFile): Plugin {

        val entry = jarFile.getJarEntry("plugin.yaml") ?: throw IllegalStateException("Plugin ${jarFile.name} has an invalid plugin.yaml")
        val inputStream = jarFile.getInputStream(entry)
        val yaml = Yaml()
        val pluginMetadata = yaml.loadAs(inputStream, PluginMetadata::class.java)
        val className = pluginMetadata.entryPoint
        val plugin = createInstance<Plugin>(className, pluginMetadata)

        Logger.getGlobal().log(Level.INFO, "Loading `${pluginMetadata.name}` plugin developed by ${pluginMetadata.author}");
        plugin.initialize()
        plugins.add(plugin)

        jarFile.close()
        return plugin
    }

}