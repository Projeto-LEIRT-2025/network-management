package com.github.project.api.router.response

data class Neighbor(

    val thisInterface: String,

    val interfaceName: String,

    val ipAddress: String,

    val mac: String

)

fun parseNeighbors(raw: String): Response<List<Neighbor>> {

    val params = raw
        .split("\n")
        .map { n -> n.split(" ").map { it.trim() }.filter { it.contains("=") } }

    val paramMap = params.map {
        it.associate { s ->
            val (key, value) = s.split("=")
            key to value.trim()
        }
    }

    val neighbors = paramMap.map { m ->
        val interfaceName = m["interface"] ?: ""
        val ipAddress = m["address"] ?: ""
        val mac = m["mac-address"] ?: ""
        val thisInterface = m["interface-name"] ?: ""

        Neighbor(
            thisInterface = thisInterface,
            interfaceName = interfaceName,
            ipAddress = ipAddress,
            mac = mac
        )
    }

    return Response(
        raw = raw,
        data = neighbors
    )
}