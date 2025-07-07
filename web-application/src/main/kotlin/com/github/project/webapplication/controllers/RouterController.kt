package com.github.project.webapplication.controllers

import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.CreateRouterDto
import com.github.project.webapplication.dto.RouterDto
import com.github.project.webapplication.dto.UpdateRouterDto
import com.github.project.networkservice.models.Router
import com.github.project.networkservice.services.RouterService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}")
class RouterController(

    private val routerService: RouterService

) {

    @Operation(summary = "Create a new router")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Router created successfully"),
        ApiResponse(responseCode = "400", description = "Router with the same management IP already exists"),
    )
    @PostMapping
    fun create(@RequestBody @Valid dto: CreateRouterDto): ResponseEntity<ApiResponseDto<RouterDto>> {

        val router = routerService.create(
            vendor = dto.vendor,
            ipAddress = dto.ipAddress,
            model = dto.model
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponseDto(
                    message = "Router created successfully",
                    data = router.toDto()
                )
            )
    }

    @Operation(summary = "Get router by id", description = "Returns a single route by id")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Router retrieved successfully"),
        ApiResponse(responseCode = "404", description = "Router not found")
    )
    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): ResponseEntity<ApiResponseDto<RouterDto>> {

        val router = routerService.getById(id)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Router retrieved successfully",
                    data = router.toDto()
                )
            )
    }

    @Operation(summary = "Get all routers", description = "Returns a list of routers")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Routers retrieved successfully")
    )
    @GetMapping
    fun getAll(): ResponseEntity<ApiResponseDto<List<RouterDto>>> {

        val routers = routerService.getAll()

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Routers retrieved successfully",
                    data = routers.map { it.toDto() }
                )
            )
    }

    @Operation(summary = "Update a router")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Routers retrieved successfully"),
        ApiResponse(responseCode = "400", description = "Router with the same management IP already exists"),
        ApiResponse(responseCode = "404", description = "Router not found")
    )
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: UpdateRouterDto): ResponseEntity<ApiResponseDto<RouterDto>> {

        val router = routerService.update(id, dto.ipAddress, dto.model, dto.vendor)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Router updated successfully",
                    data = router.toDto()
                )
            )
    }

    @Operation(summary = "Delete a router")
    @ApiResponse(
        responseCode = "200",
        description = "Router deleted successfully"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Router not found",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ApiResponseDto::class)
        )]
    )
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<ApiResponseDto<Unit>> {

        routerService.delete(id)

        return ResponseEntity
            .ok(
                ApiResponseDto(
                    message = "Router deleted successfully",
                    data = Unit
                )
            )
    }

    private fun Router.toDto() = RouterDto(
        id = this.id,
        vendor = this.vendor,
        ipAddress = this.ipAddress,
        model = this.model
    )

}