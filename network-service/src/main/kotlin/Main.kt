package com.github.projeto

fun main() {

    val routerConfiguration = RouterConfigurationImpl()
    val routerMonitoring = RouterMonitoringImpl()

    /*routerConfiguration.addStaticRoute("ether1", "192.192.192.192")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.193")
    routerConfiguration.addStaticRoute("ether1", "192.192.192.194")*/

    //routerConfiguration.setIpAddress("ether2", "23.23.23.23")
    //val response = routerConfiguration.showInterfaces()


    println(routerMonitoring.getMemoryUsed())
}