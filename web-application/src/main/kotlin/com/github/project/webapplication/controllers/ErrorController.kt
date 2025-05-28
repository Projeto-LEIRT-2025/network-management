package com.github.project.webapplication.controllers

import com.github.project.networkservice.exceptions.*
import com.github.project.webapplication.dto.ApiResponseDto
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

    @ExceptionHandler(RouterConfigurationException::class, RouterIpAddressAlreadyExistsException::class)
    fun handleBadRequest(e: Exception): ResponseEntity<ApiResponseDto<Unit>> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiResponseDto(
                    message = e.message ?: "",
                    data = Unit
                )
            )

    @ExceptionHandler(RouterLoginException::class)
    fun handleRouterLoginUnauthenticated(e: RouterLoginException): ResponseEntity<ApiResponseDto<Long>> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ApiResponseDto(
                    message = e.message,
                    data = e.routerId
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