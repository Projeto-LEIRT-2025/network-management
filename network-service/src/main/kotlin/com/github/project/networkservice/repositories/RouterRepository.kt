package com.github.project.networkservice.repositories

import com.github.project.networkservice.models.Router
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RouterRepository : JpaRepository<Router, Long>{
    fun existsByIpAddress(ipAddress: String): Boolean
}