package com.github.projeto

class RouterFactory {

    private inline fun <reified T> createInstance(className: String, vararg params: Any): T {

        val theClass = Class.forName(className)
        val constructor = theClass.getConstructor(*params.map { it.javaClass }.toTypedArray())
        val instance = constructor.newInstance(*params)

        if (instance !is T)
            throw IllegalArgumentException("Class $className is not a $instance")

        return instance
    }

    fun loadRouterConfiguration(className: String, hostname: String, port: Int, username: String, password: String): RouterConfiguration =
        createInstance(className, hostname, port.toString(), username, password)

    fun loadRouterMonitoring(className: String, hostname: String, port: Int): RouterMonitoring =
        createInstance(className, hostname, port.toString())

}