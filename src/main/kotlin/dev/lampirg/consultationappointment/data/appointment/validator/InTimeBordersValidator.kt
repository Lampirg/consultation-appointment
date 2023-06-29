package dev.lampirg.consultationappointment.data.appointment.validator

import dev.lampirg.consultationappointment.data.appointment.Appointment
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class InTimeBordersValidator : ConstraintValidator<InTimeBorders?, Appointment> {
    override fun isValid(appointment: Appointment, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val isAfterStart = appointment.startTime.isAfter(appointment.appointmentPeriod.startTime) ||
                appointment.startTime.isEqual(appointment.appointmentPeriod.startTime)
        val isBeforeEnd: Boolean = appointment.startTime.isBefore(appointment.appointmentPeriod.endTime)
        return isAfterStart && isBeforeEnd
    }
}