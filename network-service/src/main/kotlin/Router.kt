package com.github.projeto

interface Router {

    fun showInterfaces()

    fun addRoute(interfaceName: String, ipAddress: String)

    fun removeRoute(vararg number: Int)

}