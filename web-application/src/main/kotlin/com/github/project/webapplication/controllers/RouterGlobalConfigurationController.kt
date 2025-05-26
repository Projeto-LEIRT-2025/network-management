package com.github.project.webapplication.controllers

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.dto.EdgeDto
import com.github.project.networkservice.dto.GraphDto
import com.github.project.networkservice.models.Edge
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.webapplication.dto.ApiResponseDto
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
    fun getNetwork(): ResponseEntity<ApiResponseDto<GraphDto>> {

        /*val graph = routerConfigurationService.getNetworkGraph(
            mapOf(
                1L to CredentialsDto("admin", "pepe"),
                2L to CredentialsDto("admin", "pepe")
            )
        )*/

        val graph = Graph()

        graph.addNode(Node("1", "192.168.0.1"))
        graph.addNode(Node("2", "192.168.0.2"))
        graph.addNode(Node("3", "192.168.0.3"))
        graph.addNode(Node("4", "192.168.0.4"))

        graph.addEdge("1", "2")
        graph.addEdge("2", "1")
        graph.addEdge("2", "3")
        graph.addEdge("3", "2")
        graph.addEdge("3", "4")
        graph.addEdge("4", "3")
        graph.addEdge("4", "1")
        graph.addEdge("1", "4")

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Graph retrieved successfully",
                    data = GraphDto(nodes = graph.nodes, edges = graph.edges.map { EdgeDto(it.source.id, it.target.id) })
                )
            )
    }

}