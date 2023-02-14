package dev.lampirg.consultationappointment.data.appointment.validator;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InTimeBordersValidator implements ConstraintValidator<InTimeBorders, Appointment> {
    @Override
    public boolean isValid(Appointment appointment, ConstraintValidatorContext constraintValidatorContext) {
        boolean isAfterStart =
                appointment.getStartTime().isAfter(appointment.getAppointmentPeriod().getStartTime()) ||
                appointment.getStartTime().isEqual(appointment.getAppointmentPeriod().getStartTime());

        boolean isBeforeEnd = appointment.getStartTime().isBefore(
                appointment.getAppointmentPeriod().getEndTime()
        );
        return isAfterStart && isBeforeEnd;
    }
}
