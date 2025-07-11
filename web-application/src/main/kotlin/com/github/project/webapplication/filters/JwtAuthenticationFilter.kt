package com.github.project.webapplication.filters

import com.github.project.webapplication.exceptions.UserNotFoundException
import com.github.project.webapplication.repositories.UserRepository
import com.github.project.webapplication.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(

    private val jwtService: JwtService,
    private val userRepository: UserRepository

) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        val headerToken = header?.substring(7)
        val sessionToken = request.session.getAttribute("token") as? String
        val token = headerToken ?: sessionToken

        if (token != null) {

            if (jwtService.validateToken(token)) {

                try {
                    val username = jwtService.getUsername(token)
                    val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
                    val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)

                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: UserNotFoundException) {}

            }

        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI

        return path == "/error" ||
                path.startsWith("/auth") ||
                path.startsWith("/css") ||
                path.startsWith("/scripts") ||
                path.startsWith("/swagger") ||
                path.startsWith("/v3/api-docs")
    }

}