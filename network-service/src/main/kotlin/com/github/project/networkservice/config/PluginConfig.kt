package com.github.project.networkservice.config

import com.github.project.api.PluginLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PluginConfig {

    @Bean
    fun pluginLoader() = PluginLoader

}