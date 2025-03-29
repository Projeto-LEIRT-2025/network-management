package com.github.projeto

import com.github.project.api.PluginLoader

fun main() {

    PluginLoader.loadPluginsFromDirectory("/home/diogo/Área de Trabalho/ISEL/Projeto/network-management/plugin/build/libs/")
    PluginLoader.loadPlugins("/home/diogo/Área de Trabalho/router-os.jar")

    PluginLoader.listPlugins()
        .forEach { println("${it.metadata.name} plugin entry point: ${it.metadata.entryPoint}") }

    val routerConfiguration = PluginLoader.getRouterConfiguration(
        model = "router-os",
        hostname = "localhost",
        port = 2323,
        username = "admin",
        password = "pepe"
    )

    val routerMonitoring = PluginLoader.getRouterMonitoring(
        model = "router-os",
        hostname = "localhost",
        port = 161,
    )

    routerConfiguration.enableSNMP()
    routerConfiguration.changeSNMPVersion(2)

    routerMonitoring.getNetworkInterfaces().forEach { networkInterface ->
        println("${networkInterface.name} -> Bytes In: ${routerMonitoring.getBytesIn(networkInterface.index)} ; Bytes Out: ${routerMonitoring.getBytesOut(networkInterface.index)}")
    }

    //initialSetup()
}

private fun initialSetup() {

    val routerConfiguration1 = PluginLoader.getRouterConfiguration(
        model = "router-os",
        hostname = "localhost",
        port = 2323,
        username = "admin",
        password = "pepe"
    )
    val routerConfiguration2 = PluginLoader.getRouterConfiguration(
        model = "router-os",
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