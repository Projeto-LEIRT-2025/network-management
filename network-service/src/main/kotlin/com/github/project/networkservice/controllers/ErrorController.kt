package com.github.project.networkservice.controllers

import com.github.project.networkservice.dto.ApiResponseDto
import com.github.project.networkservice.exceptions.PluginLoadException
import com.github.project.networkservice.exceptions.PluginNotFoundException
import com.github.project.networkservice.exceptions.RouterNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.Exception

@RestControllerAdvice
class ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(e: MethodArgumentNotValidException): ResponseEntity<ApiResponseDto<Unit>> {
        return ResponseEntity.badRequest()
            .body(ApiResponseDto(message = e.bindingResult.fieldError?.defaultMessage ?: "", data = Unit))
    }

    @ExceptionHandler(RouterNotFoundException::class, PluginNotFoundException::class)
    fun handleNotFound(e: Exception): ResponseEntity<ApiResponseDto<Unit>> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ApiResponseDto(
                    message = e.message ?: "",
                    data = Unit
                )
            )

    @ExceptionHandler(PluginLoadException::class)
    fun handleInternalServerError(e: Exception): ResponseEntity<ApiResponseDto<Unit>> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponseDto(
                    message = e.message ?: "",
                    data = Unit
                )
            )

}