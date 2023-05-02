package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.stereotype.Service;

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
    public void deleteConsultationById(Teacher teacher, Long Id) {

    }
}
