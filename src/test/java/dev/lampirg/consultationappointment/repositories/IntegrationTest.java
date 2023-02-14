package dev.lampirg.consultationappointment.repositories;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.appointment.AppointmentRepository;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;


    @BeforeEach
    public void clear() {
        appointmentRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        Student student = studentRepository.save(StudentRepositoryTests.getStudent());
        Teacher teacher = teacherRepository.save(TeacherRepositoryTests.getTeacher());
        Appointment appointment = new Appointment();
        appointment.setTeacher(teacher);
        appointment.setStudent(student);
        appointment.setAppointmentPeriod(teacher.getDatePeriod().stream().toList().get(0));
        appointment.setStartTime(teacher.getDatePeriod().stream().toList().get(0).getStartTime());
        appointment = appointmentRepository.save(appointment);
    }

    @Test
    public void test() {
        assertEquals(1, studentRepository.findAll().size());
        assertEquals(1, teacherRepository.findAll().size());
        assertEquals(1, appointmentRepository.findAll().size());
        assertEquals(studentRepository.findAll().get(0).getAppointment().stream().toList().get(0),
                appointmentRepository.findAll().get(0));
    }

    @Test
    public void testDelete() {
        assertThrows(DataIntegrityViolationException.class, () -> teacherRepository.delete(teacherRepository.findAll().get(0)));
        assertDoesNotThrow(() -> studentRepository.delete(studentRepository.findAll().get(0)));
    }
}