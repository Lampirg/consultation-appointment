package dev.lampirg.consultationappointment.data.appointment.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [InTimeBordersValidator::class])
annotation class InTimeBorders(
    val message: String = "Invalid time borders",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)