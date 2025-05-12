package com.github.project.metricsservice.repositories

import com.github.project.metricsservice.models.DeviceStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceStatsRepository : JpaRepository<DeviceStats, Long>