package com.github.projeto

class RouterFactory {

    fun loadRouterConfiguration(className: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration {

        val theClass = ClassLoader.getSystemClassLoader().loadClass(className)

        return theClass.getConstructor(hostname.javaClass, port.javaClass, username.javaClass, password.javaClass)
            .newInstance(hostname, port, username, password) as RouterConfiguration
    }

    fun loadRouterMonitoring(className: String, hostname: String, port: Int): RouterMonitoring {

        val theClass = ClassLoader.getSystemClassLoader().loadClass(className)

        return theClass.getConstructor(hostname.javaClass, port.javaClass)
            .newInstance(hostname, port) as RouterMonitoring
    }

}