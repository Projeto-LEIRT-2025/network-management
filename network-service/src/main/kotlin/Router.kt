package com.github.projeto

interface Router {

    fun showInterfaces()

    fun addStaticRoute(interfaceName: String, ipAddress: String)

    fun removeStaticRoute(vararg number: Int)

    fun enableInterface(interfaceName: String)

    fun disableInterface(interfaceName: String)

    fun setIpAddress(interfaceName: String, ipAddress: String)

    fun removeIpAddress(vararg number: Int)

}