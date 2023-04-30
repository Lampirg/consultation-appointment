package dev.lampirg.consultationappointment.service;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SimpleAppointmentMaker implements AppointmentMaker {

    public final static int INTERVAL = 15;

    private AppointmentRepository appointmentRepository;

    public SimpleAppointmentMaker(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void makeAppointment(Teacher teacher, Student student, DatePeriod datePeriod) {
        if (!isAvailable(datePeriod))
            throw new IllegalArgumentException("Not enough time for new appointment");
        Appointment appointment = new Appointment();
        appointment.setStartTime(datePeriod.getStartTime());
        appointment.setTeacher(teacher);
        appointment.setStudent(student);
        appointment.setAppointmentPeriod(datePeriod);
        datePeriod.setUnoccupiedTime(
                datePeriod.getUnoccupiedTime().minus(Duration.ofMinutes(INTERVAL))
        );
        appointment = appointmentRepository.save(appointment);
        student.getAppointment().add(appointment);
    }

    @Override
    public boolean isAvailable(DatePeriod datePeriod) {
        return !datePeriod.getUnoccupiedTime().minus(Duration.ofMinutes(INTERVAL)).isNegative();
    }

    @Override
    public boolean isAvailable(Teacher teacher, Student student, DatePeriod datePeriod) {
        if (!isAvailable(datePeriod))
            return false;
        return student.getAppointment().stream()
                .map(Appointment::getAppointmentPeriod)
                .noneMatch(appointmentPeriod -> appointmentPeriod.equals(datePeriod));
    }
}
