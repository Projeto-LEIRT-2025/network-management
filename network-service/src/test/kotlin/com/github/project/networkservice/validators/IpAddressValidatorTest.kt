package com.github.project.networkservice.validators

import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertTrue

class IpAddressValidatorTest {

    private val regex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\$")

    @Test
    fun `valid IPv4 addresses`() {
        assertTrue(regex.matches("192.168.1.1"))
        assertTrue(regex.matches("172.19.1.255"))
        assertTrue(regex.matches("0.0.0.0"))
        assertTrue(regex.matches("255.255.255.255"))
    }

    @Test
    fun `invalid IPv4 addresses`() {
        assertFalse(regex.matches("    "))
        assertFalse(regex.matches(""))
        assertFalse(regex.matches("192.168.0.-1"))
        assertFalse(regex.matches("192.168.01.1"))
        assertFalse(regex.matches("255.1.256.3"))
        assertFalse(regex.matches("192.168.001.1"))
        assertFalse(regex.matches("1.2.3"))
    }

}