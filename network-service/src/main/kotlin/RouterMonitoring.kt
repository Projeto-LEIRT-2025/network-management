package com.github.projeto

interface RouterMonitoring {

    fun getMemoryUsed(): Int

    fun getUptime(): String

}