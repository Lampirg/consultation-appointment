package dev.lampirg.consultationappointment.service;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UpdateUserInformationAspect {

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;

    public UpdateUserInformationAspect(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @After("execution(* dev.lampirg.consultationappointment.web.StudentController.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void updateStudent() {
        Student student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        student = studentRepository.findById(student.getId()).get();
        Authentication authentication = new PreAuthenticatedAuthenticationToken(student, student.getPassword(), student.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After("execution(* dev.lampirg.consultationappointment.web.TeacherController.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void updateTeacher() {
        Teacher student = (Teacher) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        student = teacherRepository.findById(student.getId()).get();
        Authentication authentication = new PreAuthenticatedAuthenticationToken(student, student.getPassword(), student.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
