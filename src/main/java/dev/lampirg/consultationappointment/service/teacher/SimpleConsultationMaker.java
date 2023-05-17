package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class SimpleConsultationMaker implements ConsultationMaker {

    TeacherRepository teacherRepository;

    public SimpleConsultationMaker(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    @Modifying
    public void createConsultation(Teacher teacher, DatePeriod datePeriod) {
        teacher.getDatePeriods().add(datePeriod);
        teacherRepository.save(teacher);
    }
    
    @Override
    @Transactional
    @Modifying
    public void deleteConsultation(Teacher teacher, DatePeriod datePeriod) {
        teacher = teacherRepository.findById(teacher.getId()).orElseThrow();
        if (!teacher.getDatePeriods().remove(datePeriod)) {
            throw new NoSuchElementException("No datePeriod with id = " + datePeriod + " present in " + teacher + "datePeriods");
        }
        teacherRepository.save(teacher);
    }
}
