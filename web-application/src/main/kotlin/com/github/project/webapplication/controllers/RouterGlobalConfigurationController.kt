package com.github.project.webapplication.controllers

import com.github.project.api.router.response.NetworkInterface
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.webapplication.dto.EdgeDto
import com.github.project.webapplication.dto.GraphDto
import com.github.project.networkservice.models.Graph
import com.github.project.networkservice.models.Node
import com.github.project.networkservice.services.RouterConfigurationService
import com.github.project.networkservice.services.RouterMonitoringService
import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.NetworkInterfaceDto
import com.github.project.webapplication.dto.NodeDto
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

    private val routerConfigurationService: RouterConfigurationService,
    private val routerMonitoringService: RouterMonitoringService

) {

    @PostMapping("/network")
    fun getNetwork(
        @RequestBody @Valid dto: Map<Long, CredentialsDto>
    ): ResponseEntity<ApiResponseDto<GraphDto>> = runBlocking {

        val graph = routerConfigurationService.getNetworkGraph(dto)

        /*val graph = Graph()

        graph.addNode(Node("1", "192.168.0.1"))
        graph.addNode(Node("2", "192.168.0.2"))
        graph.addNode(Node("3", "192.168.0.3"))
        graph.addNode(Node("4", "192.168.0.4"))

        graph.addEdge("1", "2", "ether1", "ether2")
        graph.addEdge("2", "3", "ether1", "ether3")
        graph.addEdge("3", "4", "ether1", "ether4")
        graph.addEdge("4", "1", "ether1", "ether2")*/

        ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Graph retrieved successfully",
                    data = GraphDto(
                        nodes = graph.nodes.map { NodeDto(it.id, it.label) },
                        edges = graph.edges.map {

                            val sourceInterface = routerMonitoringService.getNetworkInterfaces(it.source.id.toLong())
                                .firstOrNull { networkInterface -> networkInterface.name == it.sourceInterface }

                            val targetInterface = routerMonitoringService.getNetworkInterfaces(it.target.id.toLong())
                                .firstOrNull { networkInterface -> networkInterface.name == it.targetInterface }

                            EdgeDto(
                                source = it.source.id,
                                target = it.target.id,
                                sourceInterface = sourceInterface?.toDto() ?: NetworkInterfaceDto(it.sourceInterface),
                                targetInterface = targetInterface?.toDto() ?: NetworkInterfaceDto(it.targetInterface)
                            )
                        }
                    )
                )
            )
    }

    private fun NetworkInterface.toDto() = NetworkInterfaceDto(
        this.name,
        this.operationalStatus
    )

}