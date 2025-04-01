package com.github.project.networkservice.services

import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Router
import com.github.project.networkservice.repositories.RouterRepository
import org.springframework.stereotype.Service

@Service
class RouterService(

    private val routerRepository: RouterRepository

) {

    fun create(vendor: String, model: String, ipAddress: String): Router {
        return routerRepository.save(
            Router(
                vendor = vendor,
                model = model,
                ipAddress = ipAddress
            )
        )
    }

    fun getAll(): List<Router> {
        return routerRepository.findAll()
    }

    fun getById(id: Long): Router {
        return routerRepository.findById(id).orElseThrow { RouterNotFoundException() }
    }

    fun delete(id: Long) {

        val router = getById(id)

        routerRepository.delete(router)
    }

}