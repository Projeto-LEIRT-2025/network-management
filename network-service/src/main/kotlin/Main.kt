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

    //routerMonitoring.getNetworkInterfaces().forEach { networkInterface ->
    //    println("${networkInterface.name} -> Bytes In: ${routerMonitoring.getBytesIn(networkInterface.index)} ; Bytes Out: ${routerMonitoring.getBytesOut(networkInterface.index)}")
    //}

    initialSetup()
}

private fun initialSetup() {

    val factory = RouterFactory()

    val routerConfiguration1 = factory.loadRouterConfiguration(
        className = "com.github.projeto.RouterConfigurationImpl",
        hostname = "localhost",
        port = 2323,
        username = "admin",
        password = "pepe"
    )
    val routerConfiguration2 = factory.loadRouterConfiguration(
        className = "com.github.projeto.RouterConfigurationImpl",
        hostname = "localhost",
        port = 3333,
        username = "admin",
        password = "pepe"
    )

    routerConfiguration1.setIpAddress("ether2", "192.168.1.2")
    routerConfiguration1.enableInterface("ether2")
    routerConfiguration1.addStaticRoute("ether2", "192.168.1.0/24")
    routerConfiguration2.addStaticRoute("192.168.1.2", "192.168.0.0/24")
    routerConfiguration2.createAddressPool("pepe-pool", "192.168.0.0/24")
    routerConfiguration2.createDHCPServerRelay("pepe-dhcp", "pepe-pool", "ether1", "192.168.0.2")
    routerConfiguration2.createDHCPServerNetwork("192.168.0.0/24", "192.168.0.2")
    routerConfiguration1.createDHCPRelay("pepe-dhcp-relay", "ether1", "192.168.1.3")
    routerConfiguration1.enableDHCPRelay("pepe-dhcp-relay")

}