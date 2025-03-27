package com.github.projeto

import java.io.File
import java.net.URLClassLoader

class RouterFactory {

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

    fun loadRouterConfiguration(className: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration =
        createInstance(className, hostname, port.toString(), username, password)

    fun loadRouterMonitoring(className: String, hostname: String, port: Int): RouterMonitoring =
        createInstance(className, hostname, port.toString())

    fun initializeClassLoader(directoryName: String) {

        val directory = File(directoryName)

        if (!directory.isDirectory)
            throw IllegalArgumentException("Directory $directory is not a directory")

        val files = directory.listFiles()
            ?.filter { file -> file.extension == "jar" } ?: emptyList()

        this.classLoader = URLClassLoader.newInstance(files.map { it.toURI().toURL() }.toTypedArray(), this::class.java.classLoader)
    }

}