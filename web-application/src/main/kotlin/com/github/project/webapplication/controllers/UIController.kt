package com.github.project.webapplication.controllers

import com.github.project.metricsservice.services.MetricsService
import com.github.project.networkservice.services.PluginService
import com.github.project.networkservice.services.RouterService
import com.github.project.webapplication.config.AppProperties
import com.github.project.webapplication.dto.toDto
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.Instant

@Controller
class UIController(

    private val routerService: RouterService,
    private val metricsService: MetricsService,
    private val pluginService: PluginService,
    private val appProperties: AppProperties

) {

    @GetMapping
    fun home() = "home"

    @GetMapping("/routers")
    fun routers(model: Model): String {

        val routers = routerService.getAll()

        model.addAttribute("routers", routers)

        return "routers"
    }

    @GetMapping("/plugins")
    fun plugins(model: Model): String {

        val plugins = pluginService.listPlugins()
            .map { it.key.toDto(it.value) }

        model.addAttribute("plugins", plugins)

        return "plugins"
    }

    @GetMapping("/auth/login")
    fun login() = "login"

    @GetMapping("/routers/{id}/metrics")
    fun deviceMetrics(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant,
        model: Model
    ): String {

        val deviceStats = metricsService.getDeviceStatsBetween(id, start, end)

        model.addAttribute("deviceStats", deviceStats)

        return "routerMetrics"
    }

    @GetMapping("/routers/{id}/interfaces/metrics")
    fun interfacesMetrics(
        @PathVariable id: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) start: Instant,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) end: Instant,
        model: Model
    ): String {

        val interfacesStats = metricsService.getInterfaceStatsBetween(
            routerId = id,
            fromTimestamp = start,
            toTimestamp = end
        )

        model.addAttribute("interfacesStats", interfacesStats)

        return "interfacesMetrics"
    }

    @GetMapping(value = ["/config.js"], produces = ["application/javascript"])
    fun configVariables(model: Model): String {
        model.addAttribute("config", appProperties)
        return "config"
    }

}