package com.github.project.webapplication.controllers

import com.github.project.networkservice.services.PluginService
import com.github.project.networkservice.services.RouterService
import com.github.project.webapplication.config.AppProperties
import com.github.project.webapplication.dto.toDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UIController(

    private val routerService: RouterService,
    private val pluginService: PluginService,
    private val appProperties: AppProperties

) {

    @GetMapping
    fun home() = "home"

    @GetMapping("/routers")
    fun routers(model: Model): String {

        val routers = routerService.getAll()

        model.addAttribute("routers", routers)

        return "routers"
    }

    @GetMapping("/plugins")
    fun plugins(model: Model): String {

        val plugins = pluginService.listPlugins()
            .map { it.key.toDto(it.value) }

        model.addAttribute("plugins", plugins)

        return "plugins"
    }

    @GetMapping("/auth/login")
    fun login() = "login"

    @GetMapping(value = ["/config.js"], produces = ["application/javascript"])
    fun configVariables(model: Model): String {
        model.addAttribute("config", appProperties)
        return "config"
    }

}