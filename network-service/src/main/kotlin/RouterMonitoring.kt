package com.github.projeto

interface RouterMonitoring {

    fun getTotalMemory(): Int

    fun getMemoryUsed(): Int

    fun getUptime(): String

    fun getCpuLoad(): Double

}