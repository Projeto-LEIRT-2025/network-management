package com.github.project.webapplication.controllers

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.dto.EdgeDto
import com.github.project.networkservice.dto.GraphDto
import com.github.project.networkservice.models.Edge
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.webapplication.dto.ApiResponseDto
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routers/configuration")
class RouterGlobalConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @GetMapping("/network")
    fun getNetwork(@RequestBody @Valid dto: Map<Long, CredentialsDto>): ResponseEntity<ApiResponseDto<GraphDto>> {

        val graph = routerConfigurationService.getNetworkGraph(dto)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Graph retrieved successfully",
                    data = GraphDto(nodes = graph.nodes, edges = graph.edges.map { EdgeDto(it.source.id, it.target.id) })
                )
            )
    }

}