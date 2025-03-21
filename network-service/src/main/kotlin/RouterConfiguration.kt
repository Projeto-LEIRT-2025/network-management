package com.github.projeto

import org.snmp4j.smi.IpAddress

interface RouterConfiguration {

    fun showInterfaces(): Response<List<NetworkInterface>>

    fun addStaticRoute(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun setIpAddress(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeIpAddress(vararg number: Int): Response<Unit>

    fun enableSNMP(): Response<Unit>

    fun disableSNMP(): Response<Unit>

    fun changeSNMPVersion(version: Int): Response<Unit>

    fun createAddressPool(name: String, address: String): Response<Unit>

    fun createDHCPServer(name: String, pool: String, interfaceName: String): Response<Unit>

}