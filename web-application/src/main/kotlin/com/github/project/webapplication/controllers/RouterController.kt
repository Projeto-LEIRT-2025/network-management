package com.github.project.webapplication.controllers

import com.github.project.webapplication.dto.ApiResponseDto
import com.github.project.webapplication.dto.CreateRouterDto
import com.github.project.webapplication.dto.RouterDto
import com.github.project.webapplication.dto.UpdateRouterDto
import com.github.project.networkservice.models.Router
import com.github.project.networkservice.services.RouterService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${ROUTERS_BASE_PATH}")
class RouterController(

    private val routerService: RouterService

) {

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