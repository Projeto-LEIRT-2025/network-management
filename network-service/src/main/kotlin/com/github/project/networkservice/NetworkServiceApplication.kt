package com.github.project.networkservice

import com.github.project.api.PluginLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NetworkServiceApplication

fun main(args: Array<String>) {
    PluginLoader.loadPluginsFromDirectory("/plugins")
    runApplication<NetworkServiceApplication>(*args)
}