package com.github.project.webapplication.validators

import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertTrue

class IpAddressValidatorTest {

    private val validator = IpAddressValidator()

    @Test
    fun `valid IPv4 addresses`() {
        assertTrue(validator.isValid("192.168.1.1", null))
        assertTrue(validator.isValid("172.19.1.255", null))
        assertTrue(validator.isValid("0.0.0.0", null))
        assertTrue(validator.isValid("255.255.255.255", null))
    }

    @Test
    fun `invalid IPv4 addresses`() {
        assertFalse(validator.isValid("    ", null))
        assertFalse(validator.isValid("", null))
        assertFalse(validator.isValid("192.168.0.-1", null))
        assertFalse(validator.isValid("192.168.01.1", null))
        assertFalse(validator.isValid("255.1.256.3", null))
        assertFalse(validator.isValid("192.168.001.1", null))
        assertFalse(validator.isValid("1.2.3", null))
        assertFalse(validator.isValid("192.168.0.1.3", null))
    }

}