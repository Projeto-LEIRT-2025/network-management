package com.github.project.webapplication.controllers

import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.dto.EdgeDto
import com.github.project.networkservice.dto.GraphDto
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.webapplication.dto.ApiResponseDto
import jakarta.validation.Valid
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/routers/configuration")
class RouterGlobalConfigurationController(

    private val routerConfigurationService: RouterConfigurationService

) {

    @PostMapping("/network")
    fun getNetwork(
        @RequestBody @Valid dto: Map<Long, CredentialsDto>
    ): ResponseEntity<ApiResponseDto<GraphDto>> = runBlocking {

        //val graph = routerConfigurationService.getNetworkGraph(dto)

        val graph = Graph()

        graph.addNode(Node("1", "192.168.0.1"))
        graph.addNode(Node("2", "192.168.0.2"))
        graph.addNode(Node("3", "192.168.0.3"))
        graph.addNode(Node("4", "192.168.0.4"))
        graph.addNode(Node("5", "192.168.0.5"))
        graph.addNode(Node("6", "192.168.0.6"))
        graph.addNode(Node("7", "192.168.0.7"))
        graph.addNode(Node("8", "192.168.0.8"))
        graph.addNode(Node("9", "192.168.0.9"))
        graph.addNode(Node("10", "192.168.0.10"))
        graph.addNode(Node("11", "192.168.0.11"))
        graph.addNode(Node("12", "192.168.0.12"))
        graph.addNode(Node("13", "192.168.0.13"))
        graph.addNode(Node("14", "192.168.0.14"))
        graph.addNode(Node("15", "192.168.0.14"))
        graph.addNode(Node("16", "192.168.0.14"))
        graph.addNode(Node("17", "192.168.0.14"))
        graph.addNode(Node("18", "192.168.0.14"))
        graph.addNode(Node("19", "192.168.0.14"))
        graph.addNode(Node("20", "192.168.0.14"))
        graph.addNode(Node("21", "192.168.0.14"))
        graph.addNode(Node("22", "192.168.0.14"))
        graph.addNode(Node("23", "192.168.0.14"))
        graph.addNode(Node("24", "192.168.0.14"))
        graph.addNode(Node("25", "192.168.0.14"))

        graph.addNode(Node("4", "192.168.1.1"))
        graph.addNode(Node("5", "192.168.1.2"))
        graph.addNode(Node("6", "192.168.1.6"))

        graph.addNode(Node("7", "192.168.2.1"))
        graph.addNode(Node("8", "192.168.2.2"))
        graph.addNode(Node("9", "192.168.2.9"))


        graph.addEdge("1", "2")
        graph.addEdge("2", "3")
        graph.addEdge("4", "1")
        graph.addEdge("5", "2")
        graph.addEdge("6", "5")
        graph.addEdge("7", "6")
        graph.addEdge("8", "7")
        graph.addEdge("9", "8")
        graph.addEdge("10", "9")
        graph.addEdge("11", "10")
        graph.addEdge("12", "11")
        graph.addEdge("13", "12")
        graph.addEdge("14", "13")
        graph.addEdge("15", "14")
        graph.addEdge("16", "15")
        graph.addEdge("17", "16")
        graph.addEdge("18", "17")
        graph.addEdge("19", "18")
        graph.addEdge("20", "19")
        graph.addEdge("21", "20")
        graph.addEdge("22", "21")
        graph.addEdge("23", "22")
        graph.addEdge("24", "23")
        graph.addEdge("25", "24")


        graph.addEdge("4", "5")
        graph.addEdge("5", "6")


        graph.addEdge("7", "8")
        graph.addEdge("8", "9")

        graph.addEdge("3", "4")  // Liga rede 0 à rede 1
        graph.addEdge("6", "7")  // Liga rede 1 à rede 2

        ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Graph retrieved successfully",
                    data = GraphDto(nodes = graph.nodes, edges = graph.edges.map { EdgeDto(it.source.id, it.target.id) })
                )
            )
    }

}