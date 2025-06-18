package com.github.project.webapplication.controllers

import com.github.project.networkservice.services.RouterService
import com.github.project.webapplication.config.AppProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UIController(

    private val routerService: RouterService,
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

    @GetMapping(value = ["/config.js"], produces = ["application/javascript"])
    fun configVariables(model: Model): String {
        model.addAttribute("config", appProperties)
        return "config"
    }

}