package com.github.projeto

data class NetworkInterface(

    val number: Int,
    val status: Char,
    val interfaceName: String,
    val type: String,
    val actualMtu: Int,
    val l2Mtu: Int?,
    val macAddress: String

)

fun parseNetworkInterfaces(data: String): List<NetworkInterface> {

    val lines = data.lines().drop(3)
    val interfaces = mutableListOf<NetworkInterface>()

    for (line in lines) {

        val parts = line.trim().split(Regex("\\s+"))
        val number = parts[0].toIntOrNull() ?: 0
        val status = parts[1].first()
        val name = parts[2]
        val type = parts[3]
        val actualMtu = parts[4].toIntOrNull() ?: 0
        val l2Mtu = parts.getOrNull(5)?.toIntOrNull()
        val macAddress = parts.last()

        interfaces.add(NetworkInterface(number, status, name, type, actualMtu, l2Mtu, macAddress))
    }

    return interfaces
}
