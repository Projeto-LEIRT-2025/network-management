package com.github.project.webapplication.controllers

import com.github.project.networkservice.services.RouterService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UIController(

    private val routerService: RouterService

) {

    @GetMapping
    fun home() = "home"

    @GetMapping("/routers")
    fun routers(model: Model): String {

        val routers = routerService.getAll()

        model.addAttribute("routers", routers)

        return "routers"
    }

}