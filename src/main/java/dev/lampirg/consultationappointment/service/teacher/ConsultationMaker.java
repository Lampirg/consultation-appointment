package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;

public interface ConsultationMaker {
    void createConsultation(Teacher teacher, DatePeriod datePeriod);
    void deleteConsultationById(Teacher teacher, Long datePeriodId);
}
