package com.github.projeto

fun main() {

    val routerConfiguration = RouterConfigurationImpl()
    val routerMonitoring = RouterMonitoringImpl()

    /*routerConfiguration.addStaticRoute("ether1", "192.192.192.192")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.193")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.194")*/

    //routerConfiguration.setIpAddress("ether2", "23.23.23.23")
    //val response = routerConfiguration.showInterfaces()

    routerConfiguration.enableSNMP()
    routerConfiguration.changeSNMPVersion(2)
    println(routerMonitoring.getCpuLoad())

    println(routerConfiguration.createAddressPool("pepe-pool", "172.0.1.0/24"))
    println(routerConfiguration.createDHCPServer("pepe-dhcp", "pepe-pool", "ether1"))
}