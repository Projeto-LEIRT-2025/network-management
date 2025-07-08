package com.github.project.api.router.response

data class NetworkInterface(

    val index: Int,
    val name: String,
    val actualMtu: Int,
    val macAddress: String,
    val operationalStatus: OperationalStatus

) {

    init {
        require(index > 0) { "Index must be greater than zero." }
        require(name.isNotBlank()) { "Name must not be blank." }
        require(actualMtu > 0) { "Mtu must be greater than zero." }
        require(macAddress.isNotBlank()) { "MAC Address must not be blank." }
    }

    enum class OperationalStatus(val value: Int) {

        UP(1),
        DOWN(2),
        TESTING(3),
        UNKNOWN(4),
        DORMANT(5),
        NOT_PRESENT(6),
        LOWER_LAYER_DOWN(7);

        companion object {

            fun getFromValue(value: Int): OperationalStatus {
                return entries.first { it.value == value }
            }

        }

    }

    class Builder {

        var index: Int = 0
        var name: String = ""
        var actualMtu: Int = 0
        var macAddress: String = ""
        var operationalStatus: OperationalStatus = OperationalStatus.UNKNOWN

        fun index(index: Int) = apply { this.index = index }
        fun name(name: String) = apply { this.name = name }
        fun actualMtu(actualMtu: Int) = apply { this.actualMtu = actualMtu }
        fun macAddress(macAddress: String) = apply { this.macAddress = macAddress }
        fun operationalStatus(operationalStatus: OperationalStatus) = apply { this.operationalStatus = operationalStatus }

        fun build() = NetworkInterface(index, name, actualMtu, macAddress, operationalStatus)
    }

}