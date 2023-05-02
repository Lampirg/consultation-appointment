package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SimpleConsultationMaker implements ConsultationMaker {

    TeacherRepository teacherRepository;

    public SimpleConsultationMaker(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void createConsultation(Teacher teacher, DatePeriod datePeriod) {
        teacher.getDatePeriods().add(datePeriod);
        teacherRepository.save(teacher);
    }
    
    @Override
    public void deleteConsultationById(Teacher teacher, Long datePeriodId) {
        if (!teacher.getDatePeriods().remove(teacherRepository.findDatePeriodById(datePeriodId).orElseThrow())) {
            throw new NoSuchElementException("No datePeriod with id = " + datePeriodId + " present in " + teacher + "datePeriods");
        }
        teacherRepository.save(teacher);
    }
}
