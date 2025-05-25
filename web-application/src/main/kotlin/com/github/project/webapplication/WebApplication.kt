package com.github.project.webapplication

import com.github.project.api.PluginLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = ["com.github.project"])
@EnableJpaRepositories(basePackages = ["com.github.project"])
@EntityScan(basePackages = ["com.github.project"])
class WebApplication

fun main(args: Array<String>) {
    //PluginLoader.loadPluginsFromDirectory("/plugins")
    runApplication<WebApplication>(*args)
}