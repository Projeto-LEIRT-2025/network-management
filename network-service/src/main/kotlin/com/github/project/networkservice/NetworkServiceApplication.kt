package com.github.project.networkservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NetworkServiceApplication

fun main(args: Array<String>) {
    runApplication<NetworkServiceApplication>(*args)
}