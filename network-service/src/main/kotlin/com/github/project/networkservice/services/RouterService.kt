package com.github.project.networkservice.services

import com.github.project.networkservice.exceptions.RouterIpAddressAlreadyExistsException
import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Router
import com.github.project.networkservice.repositories.RouterRepository
import org.springframework.stereotype.Service

/**
 *
 * This class is responsible to manage all the routers
 *
 */

@Service
class RouterService(

    private val routerRepository: RouterRepository

) {

    /**
     *
     * Creates a router given the vendor, model and ip address
     *
     * @param vendor the vendor
     * @param model the model
     * @param ipAddress the ip address
     *
     * @return the Router created
     *
     */

    fun create(vendor: String, model: String, ipAddress: String): Router {

        if(routerRepository.existsByIpAddress(ipAddress))
            throw RouterIpAddressAlreadyExistsException()

        return routerRepository.save(
            Router(
                vendor = vendor,
                model = model,
                ipAddress = ipAddress
            )
        )
    }

    /**
     *
     * Returns all the routers from the system
     *
     * @return List of routers
     *
     */

    fun getAll(): List<Router> {
        return routerRepository.findAll()
    }

    /**
     *
     * Get a router given an id
     *
     * @return the router
     *
     * @throws RouterNotFoundException if the router was not found
     *
     */

    fun getById(id: Long): Router {
        return routerRepository.findById(id).orElseThrow { RouterNotFoundException() }
    }

    /**
     *
     * Delete router given an id
     *
     * @param id the id of router
     *
     * @throws RouterNotFoundException if the router was not found
     *
     */

    fun delete(id: Long) {

        val router = getById(id)

        routerRepository.delete(router)
    }

    /**
     *
     * Update the router given an id
     *
     * @param id the id of router
     * @param ipAddress the ip address of router
     * @param model the model of router
     * @param vendor the vendor of router
     *
     * @return the updated router
     *
     */

    fun update(id: Long, ipAddress: String?, model: String?, vendor: String?): Router {

        val router = getById(id)

        return routerRepository.save(
            router.copy(
                model = model ?: router.model,
                vendor = vendor ?: router.vendor,
                ipAddress = ipAddress ?: router.ipAddress
            )
        )
    }

}