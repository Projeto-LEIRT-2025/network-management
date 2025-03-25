package com.github.projeto

fun main() {

    val factory = RouterFactory()
    val routerConfiguration = factory.loadRouterConfiguration(
        className = "com.github.projeto.RouterConfigurationImpl",
        hostname = "localhost",
        port = 2323,
        username = "admin",
        password = "pepe"
    )
    val routerMonitoring = factory.loadRouterMonitoring(
        className = "com.github.projeto.RouterMonitoringImpl",
        hostname = "localhost",
        port = 161,
    )

    /*routerConfiguration.addStaticRoute("ether1", "192.192.192.192")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.193")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.194")*/

    //routerConfiguration.setIpAddress("ether2", "23.23.23.23")
    //val response = routerConfiguration.showInterfaces()

    //routerConfiguration.enableSNMP()
    //routerConfiguration.changeSNMPVersion(2)

    routerMonitoring.getNetworkInterfaces().forEach { networkInterface ->
        println("${networkInterface.name} -> Bytes In: ${routerMonitoring.getBytesIn(networkInterface.index)} ; Bytes Out: ${routerMonitoring.getBytesOut(networkInterface.index)}")
    }

    //println(routerConfiguration.createAddressPool("pepe-pool", "192.168.1.0/24"))
    //println(routerConfiguration.createDHCPServer("pepe-dhcp", "pepe-pool", "ether1"))
    //routerConfiguration.setIpAddress("ether2", "192.168.1.3")
    //routerConfiguration.enableInterface("ether2")
    //routerConfiguration.addStaticRoute("ether2", "192.168.1.0/24")
    //routerConfiguration.createDHCPRelay("pepe-dhcp-relay", "ether1", "192.168.1.3")
    //routerConfiguration.enableDHCPRelay("pepe-dhcp-relay")
}