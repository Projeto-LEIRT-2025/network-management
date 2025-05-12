package com.github.project.metricsservice.repositories

import com.github.project.metricsservice.models.InterfaceStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface InterfaceStatsRepository : JpaRepository<InterfaceStats, Long> {

    fun findByTimestampBetween(fromTimestamp: Instant, toTimestamp: Instant): List<InterfaceStats>

}