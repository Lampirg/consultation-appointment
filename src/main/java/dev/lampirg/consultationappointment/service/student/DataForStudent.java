package dev.lampirg.consultationappointment.service.student;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataForStudent {

    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;

    public DataForStudent(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public Student findStudentById(Integer id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Teacher findTeacherById(Integer id) {
        return teacherRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public DatePeriod findDatePeriodById(Long id) {
        return teacherRepository.findDatePeriodById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }
}
