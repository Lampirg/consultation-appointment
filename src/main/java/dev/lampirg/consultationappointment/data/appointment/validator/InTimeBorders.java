package dev.lampirg.consultationappointment.data.appointment.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InTimeBordersValidator.class)
public @interface InTimeBorders {
    String message() default "Invalid time borders";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
