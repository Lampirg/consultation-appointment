package dev.lampirg.consultationappointment.service.student;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
public class SimpleAppointmentMaker implements AppointmentMaker {

    public final static Duration INTERVAL = Duration.ofMinutes(15);

    private AppointmentRepository appointmentRepository;
    private StudentRepository studentRepository;

    public SimpleAppointmentMaker(AppointmentRepository appointmentRepository, StudentRepository studentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void makeAppointment(Teacher teacher, Student student, DatePeriod datePeriod) {
        if (!isAvailable(teacher, student, datePeriod))
            throw new IllegalArgumentException("Not enough time for new appointment");
        Appointment appointment = new Appointment();
        appointment.setStartTime(datePeriod.getStartTime());
        appointment.setTeacher(teacher);
        appointment.setStudent(student);
        appointment.setAppointmentPeriod(datePeriod);
        datePeriod.setUnoccupiedTime(
                datePeriod.getUnoccupiedTime().minus(INTERVAL)
        );
        appointment = appointmentRepository.save(appointment);
        studentRepository.findById(appointment.getStudent().getId()).get().getAppointments().add(appointment);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.getAppointmentPeriod().setUnoccupiedTime(
                appointment.getAppointmentPeriod().getUnoccupiedTime().plus(INTERVAL)
        );
        appointmentRepository.save(appointment);
        appointmentRepository.delete(appointment);
        studentRepository.findById(appointment.getStudent().getId()).get().getAppointments().remove(appointment);
    }

    @Override
    public boolean isAvailable(DatePeriod datePeriod) {
        return !datePeriod.getUnoccupiedTime().minus(INTERVAL).isNegative() &&
                (datePeriod.getStartTime().toLocalDate().isAfter(LocalDate.now()) ||
                        !datePeriod.getAppointments().isEmpty());
    }

    @Override
    public boolean isAvailable(Teacher teacher, Student student, DatePeriod datePeriod) {
        if (!isAvailable(datePeriod))
            return false;
        return student.getAppointments().stream()
                .map(Appointment::getAppointmentPeriod)
                .noneMatch(appointmentPeriod -> appointmentPeriod.equals(datePeriod));
    }
}
