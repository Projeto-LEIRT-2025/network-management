package com.github.project.webapplication.controllers

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UIErrorController {

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError(e: Exception, model: Model): String {

        model.addAttribute("error", e.message ?: "Something went wrong")

        return "error"
    }

}