package com.github.projeto

import org.snmp4j.smi.IpAddress

interface RouterConfiguration {

    //INTERFACES

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun setIpAddress(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeIpAddress(vararg number: Int): Response<Unit>

    //DHCP

    fun createAddressPool(name: String, address: String): Response<Unit>

    fun createDHCPServer(name: String, pool: String, interfaceName: String): Response<Unit>

    fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String)

    fun enableDHCPRelay(name: String)

    fun disableDHCPRelay(name: String)

    fun removeDHCPRelay(name: String)

    //ROUTES

    fun addStaticRoute(gateway: String, ipAddress: String): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    //SNMP

    fun enableSNMP(): Response<Unit>

    fun disableSNMP(): Response<Unit>

    fun changeSNMPVersion(version: Int): Response<Unit>

}