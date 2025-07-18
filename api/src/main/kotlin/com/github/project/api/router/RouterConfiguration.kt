package com.github.project.api.router

import com.github.project.api.router.response.InterfaceMac
import com.github.project.api.router.response.Neighbor
import com.github.project.api.router.response.Response

interface RouterConfiguration {

    fun login(): Boolean

    //INTERFACES

    fun enableInterface(interfaceName: String): Response<Unit>

    fun disableInterface(interfaceName: String): Response<Unit>

    fun getInterfacesMac(): Response<List<InterfaceMac>>

    fun setIpAddress(interfaceName: String, ipAddress: String, prefix: Int): Response<Unit>

    fun removeIpAddress(interfaceName: String): Response<Unit>

    //DHCP

    fun createAddressPool(name: String, rangeStart: String, rangeEnd: String): Response<Unit>

    fun createDHCPServer(name: String, pool: String, interfaceName: String): Response<Unit>

    fun createDHCPServerRelay(name: String, pool: String, interfaceName: String, relayAddress: String): Response<Unit>

    fun createDHCPServerNetwork(network: String, prefix: Int, gateway: String): Response<Unit>

    fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String): Response<Unit>

    fun enableDHCPRelay(name: String): Response<Unit>

    fun disableDHCPRelay(name: String): Response<Unit>

    fun removeDHCPRelay(name: String): Response<Unit>

    //ROUTES

    fun addStaticRoute(gateway: String, ipAddress: String, prefix: Int): Response<Unit>

    fun removeStaticRoute(vararg number: Int): Response<Unit>

    //SNMP

    fun enableSNMP(): Response<Unit>

    fun disableSNMP(): Response<Unit>

    fun changeSNMPVersion(version: Int): Response<Unit>

    //OSPF

    fun createOSPFProcess(processId: String, routerId: String): Response<Unit>

    fun createOSPFArea(areaId: String, processId: String): Response<Unit>
    
    fun addOSPFNetworks(network: String, prefix: Int, areaName: String): Response<Unit>

    fun addOSPFInterface(interfaceName: String, areaName: String, networkType: String, cost: Int): Response<Unit>

    //NEIGHBORS

    fun getNeighbors(): Response<List<Neighbor>>
}