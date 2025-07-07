package com.github.project.webapplication.controllers

import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.LoginDto
import com.github.project.webapplication.dto.UserDto
import com.github.project.webapplication.services.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(

    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService

) {

    @Operation(summary = "Authenticate a user", description = "used in the login phase to authenticate a user into the application")
    @ApiResponse(
        responseCode = "200",
        description = "User authenticate successfully"
    )
    @ApiResponse(
        responseCode = "401",
        description = "Wrong username or password introduced",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )

    @PostMapping("/login")
    fun login(@RequestBody @Valid dto: LoginDto, session: HttpSession): ResponseEntity<UserDto> {

        val usernameAndPassword = UsernamePasswordAuthenticationToken(dto.username, dto.password)
        authenticationManager.authenticate(usernameAndPassword)
        val token = jwtService.generateToken(dto.username)

        session.setAttribute("token", token)

        return ResponseEntity
            .ok(UserDto(username = dto.username, token = token))
    }

}