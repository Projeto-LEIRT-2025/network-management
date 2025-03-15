package com.github.projeto

fun main() {

    val router = RouterImpl()

    /*router.addStaticRoute("ether1", "192.192.192.192")
    router.addStaticRoute("ether1", "192.192.192.193")
    router.addStaticRoute("ether1", "192.192.192.194")*/

    router.setIpAddress("ether2", "23.23.23.23")
    router.showInterfaces()
}