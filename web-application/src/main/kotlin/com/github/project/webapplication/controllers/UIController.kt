package com.github.project.webapplication.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UIController {

    @GetMapping
    fun home(model: Model): String {

        model.addAttribute("name", "João")

        return "home"
    }

}