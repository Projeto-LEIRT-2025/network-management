package com.github.projeto

interface RouterMonitoring {

    fun getTotalMemory(): Int

    fun getMemoryUsed(): Int

    fun getUptime(): String

    fun getCpuLoad(): Double

    fun getNetworkInterfaces(): List<NetworkInterface>

    fun getBytesIn(index: Int): Long

    fun getBytesOut(index: Int): Long

}