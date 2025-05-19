package com.github.project.webapplication.controllers

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.models.Edge
import com.github.project.networkservice.services.RouterConfigurationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routers/configuration")
class RouterGlobalConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @GetMapping("/network")
    fun getNetwork(): ResponseEntity<List<Edge>> {

        val edges = routerConfigurationService.getNetworkGraph(
            mapOf(
                1L to CredentialsDto("admin", "pepe"),
                2L to CredentialsDto("admin", "pepe")
            )
        ).edges

        return ResponseEntity.ok(edges)
    }

}