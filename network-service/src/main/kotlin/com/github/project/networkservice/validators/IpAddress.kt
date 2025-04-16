package com.github.project.networkservice.validators

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [IpAddressValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class IpAddress(

    val message: String = "Invalid IP Address",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []

)

class IpAddressValidator : ConstraintValidator<IpAddress, String> {

    private val ipv4Regex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\$")

    override fun isValid(p0: String?, p1: ConstraintValidatorContext?): Boolean {
        return !p0.isNullOrBlank() && ipv4Regex.matches(p0)
    }

}
