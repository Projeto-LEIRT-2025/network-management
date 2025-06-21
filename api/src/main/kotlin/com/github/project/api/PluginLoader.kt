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

    val pluginsDirectoryName = System.getenv("PLUGINS_DIRECTORY")
    private val pluginsDirectory = File(pluginsDirectoryName)
    private val plugins = mutableMapOf<Plugin, Boolean>()
    private var classLoader: ClassLoader = this.javaClass.classLoader

    init {

        require(pluginsDirectory.exists() && pluginsDirectory.isDirectory) {
            "Plugins directory does not exist: $pluginsDirectory"
        }

    }

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

        val theClass = this.plugins
            .filter { it.value }
            .keys
            .firstNotNullOfOrNull { it.routerConfigurationManager.get(model) } ?: return null

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

        val theClass = this.plugins
            .filter { it.value }
            .keys.firstNotNullOfOrNull { it.routerMonitoringManager.get(model) } ?: return null

        return createInstance(theClass.name, hostname, port.toString())
    }

    fun uploadPlugin(fileName: String, content: ByteArray): Plugin {

        val file = File(pluginsDirectory, fileName)

        file.writeBytes(content)

        Logger.getGlobal().log(Level.INFO, "Plugin uploaded: ${file.absolutePath}")

        return loadPlugin(file)
    }

    /**
     *
     * Returns a map of all loaded plugins along with their enabled status.
     *
     * @return a map where the key is the loaded [Plugin] instance, and the value is a [Boolean]
     * indicating whether the plugin is currently enabled (`true`) or disabled (`false`)
     *
     */

    fun listPlugins(): Map<Plugin, Boolean> =
        this.plugins

    /**
     *
     * Get the plugin if it is enabled
     *
     * @param name name of the plugin
     *
     * @return the plugin or null if does not exist
     *
     */

    fun getPlugin(name: String): Pair<Plugin, Boolean>? {
        return this.plugins.entries.firstOrNull { it.key.metadata.name == name }?.toPair()
    }

    /**
     *
     * Disable the plugin
     *
     * @param plugin plugin to disable
     *
     */

    fun disablePlugin(plugin: Plugin) {
        plugin.disable()
        plugins[plugin] = false
    }

    /**
     *
     * Enable the plugin
     *
     * @param plugin plugin to enable
     *
     */

    fun enablePlugin(plugin: Plugin) {
        plugin.enable()
        plugins[plugin] = true
    }

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
     * Load plugin from JarFile, but the plugin is not initialized,
     * it needs to call enable method
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

        Logger.getGlobal().log(Level.INFO, "Loaded `${pluginMetadata.name}` plugin developed by ${pluginMetadata.author}");
        plugins[plugin] = false

        jarFile.close()
        return plugin
    }

}