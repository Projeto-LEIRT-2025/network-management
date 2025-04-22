package com.github.project.api.router

import com.github.project.api.router.response.Response

interface RouterConfiguration {

    //INTERFACES

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun setIpAddress(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeIpAddress(vararg number: Int): Response<Unit>

    //DHCP

    fun createAddressPool(name: String, address: String): Response<Unit>

    fun createDHCPServer(name: String, pool: String, interfaceName: String): Response<Unit>

    fun createDHCPServerRelay(name: String, pool: String, interfaceName: String, relayAddress: String): Response<Unit>

    fun createDHCPServerNetwork(network: String, gateway: String): Response<Unit>

    fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String)

    fun enableDHCPRelay(name: String)

    fun disableDHCPRelay(name: String)

    fun removeDHCPRelay(name: String)

    //ROUTES

    fun addStaticRoute(gateway: String, ipAddress: String, mask: Int): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    //SNMP

    fun enableSNMP(): Response<Unit>

    fun disableSNMP(): Response<Unit>

    fun changeSNMPVersion(version: Int): Response<Unit>

    //OSPF

    fun createOSPFProcess(processId: String, routerId: String)

    fun createOSPFArea(areaId: String, processId: String)

    fun addOSPFNetworks(network: String, mask: Int, areaName: String)

    fun addOSPFInterface(interfaceName: String, areaName: String, networkType: String, cost: Int)

    //NEIGHBORS

    fun getNeighbors(): Response<Unit>
}