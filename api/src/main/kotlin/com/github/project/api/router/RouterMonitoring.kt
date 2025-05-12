package com.github.project.api.router

import com.github.project.api.router.response.NetworkInterface

interface RouterMonitoring {

    fun getTotalMemory(): Int

    fun getMemoryUsage(): Int

    fun getUptime(): String

    fun getCpuUsage(): Double

    fun getNetworkInterfaces(): List<NetworkInterface>

    fun getBytesIn(index: Int): Long

    fun getBytesOut(index: Int): Long

    fun getPacketsIn(index: Int): Long

    fun getPacketsOut(index: Int): Long

    fun getErrorPacketsIn(index: Int): Long

    fun getErrorPacketsOut(index: Int): Long

    fun getDiscardedPacketsIn(index: Int): Long

    fun getDiscardedPacketsOut(index: Int): Long

}