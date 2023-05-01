package dev.lampirg.consultationappointment.service;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;

public interface AppointmentMaker {
    void makeAppointment(Teacher teacher, Student student, DatePeriod datePeriod);
    void deleteAppointmentById(Long id);
    boolean isAvailable(DatePeriod datePeriod);

    default boolean isAvailable(Teacher teacher, Student student, DatePeriod datePeriod) {
        return isAvailable(datePeriod);
    }
}
