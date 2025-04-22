package com.github.project.api.router

import com.github.project.api.router.response.Response

interface RouterConfiguration {

    //INTERFACES

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun setIpAddress(interfaceName: String, ipAddress: String): Response<Unit>

    fun removeIpAddress(vararg number: Int): Response<Unit>

    //DHCP

    fun createAddressPool(name: String, address: String, mask: Int): Response<Unit>

    fun createDHCPServer(name: String, pool: String, interfaceName: String): Response<Unit>

    fun createDHCPServerRelay(name: String, pool: String, interfaceName: String, relayAddress: String): Response<Unit>

    fun createDHCPServerNetwork(network: String, gateway: String): Response<Unit>

    fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String): Response<Unit>

    fun enableDHCPRelay(name: String): Response<Unit>

    fun disableDHCPRelay(name: String): Response<Unit>

    fun removeDHCPRelay(name: String): Response<Unit>

    //ROUTES

    fun addStaticRoute(gateway: String, ipAddress: String, mask: Int): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    //SNMP

    fun enableSNMP(): Response<Unit>

    fun disableSNMP(): Response<Unit>

    fun changeSNMPVersion(version: Int): Response<Unit>

    //OSPF

    fun createOSPFProcess(processId: String, routerId: String): Response<Unit>

    fun createOSPFArea(areaId: String, processId: String): Response<Unit>
    
    fun addOSPFNetworks(network: String, mask: Int, areaName: String): Response<Unit>

    fun addOSPFInterface(interfaceName: String, areaName: String, networkType: String, cost: Int): Response<Unit>

    //NEIGHBORS

    fun getNeighbors(): Response<Unit>
}