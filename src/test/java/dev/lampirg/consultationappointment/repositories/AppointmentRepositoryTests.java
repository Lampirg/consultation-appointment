package dev.lampirg.consultationappointment.repositories;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: fix these mocking
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointmentRepositoryTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @BeforeAll
    public void mock() {
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(StudentRepositoryTests.getStudent()));
        Mockito.when(teacherRepository.findById(1)).thenReturn(Optional.of(TeacherRepositoryTests.getTeacher()));
    }

    @BeforeEach
    public void clearAndAdd() {
        appointmentRepository.deleteAll();
        appointmentRepository.save(getAppointment(studentRepository, teacherRepository));
    }

    @Test
    void testFindById() {
        Appointment appointment = appointmentRepository.save(getAppointment(studentRepository, teacherRepository));
        Appointment result = appointmentRepository.findById(appointment.getId()).get();
        assertEquals(appointment.getId(), result.getId());
        assertEquals(2, appointmentRepository.findAll().size());
    }

    public static Appointment getAppointment(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        Appointment appointment = new Appointment();
        appointment.setStudent(studentRepository.findById(1).get());
        appointment.setTeacher(teacherRepository.findById(1).get());
        appointment.setAppointmentPeriod(teacherRepository.findById(1).get().getDatePeriods().stream().toList().get(0));
        appointment.setStartTime(teacherRepository.findById(1).get().getDatePeriods().stream().toList().get(0).getStartTime());
        return appointment;
    }

}
