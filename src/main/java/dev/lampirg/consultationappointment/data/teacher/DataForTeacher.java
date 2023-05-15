package dev.lampirg.consultationappointment.data.teacher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataForTeacher {

    private TeacherRepository teacherRepository;

    public DataForTeacher(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional(readOnly = true)
    public Teacher findTeacherById(Integer id) {
        return teacherRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public DatePeriod findDatePeriodById(Long id) {
        return teacherRepository.findDatePeriodById(id).orElseThrow();
    }
}
