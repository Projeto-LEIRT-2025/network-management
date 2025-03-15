package com.github.projeto

fun main() {

    val router = RouterImpl()

    /*router.showInterfaces()
    router.addRoute("ether1", "192.192.192.192")
    router.addRoute("ether1", "192.192.192.193")
    router.addRoute("ether1", "192.192.192.194")*/

    router.showInterfaces()
    router.setIpAddress("ether2", "23.23.23.23")
}