package com.github.projeto

interface RouterConfiguration {

    fun showInterfaces(): Response<List<NetworkInterface>>

    fun addStaticRoute(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun setIpAddress(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeIpAddress(vararg number: Int): Response<Unit>

}