package com.github.project.networkservice.services

import com.github.project.networkservice.exceptions.RouterNotFoundException
import com.github.project.networkservice.models.Router
import com.github.project.networkservice.repositories.RouterRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class RouterServiceTest {

    @Mock
    private lateinit var routerRepository: RouterRepository

    @InjectMocks
    private lateinit var routerService: RouterService

    @Test
    fun `create a router should succeed`() {

        val expected = Router(
            id = 1,
            vendor = "Mikrotik",
            ipAddress = "192.168.0.1",
            model = "Router OS"
        )

        Mockito.`when`(routerRepository.save(expected.copy(id = 0))).thenReturn(expected)
        val actual = routerService.create(vendor = expected.vendor, ipAddress = expected.ipAddress, model = expected.model)

        assertEquals(expected, actual)
    }

    @Test
    fun `get a router that doesn't exist should fail`() {

        Mockito.`when`(routerRepository.findById(1)).thenReturn(Optional.empty())

        assertThrows<RouterNotFoundException> {
            routerService.getById(1)
        }

    }

    @Test
    fun `get a router that exists should succeed`() {

        val expected = Router(
            id = 1,
            vendor = "Mikrotik",
            ipAddress = "192.168.0.1",
            model = "Router OS"
        )

        Mockito.`when`(routerRepository.findById(1)).thenReturn(Optional.of(expected))
        val actual = routerService.getById(1)

        assertEquals(expected, actual)
    }

}