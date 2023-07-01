package dev.lampirg.consultationappointment.service.teacher

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.time.ZoneId

@Aspect
@Component
class AutoDeleteConsultationAspect @Autowired constructor(
    private val teacherRepository: TeacherRepository,
    private val consultationMaker: ConsultationMaker,
    private val scheduler: TaskScheduler
) {
    @AfterReturning(
        "execution(* dev.lampirg.consultationappointment.service.teacher.ConsultationMaker.createConsultation(..)) && " +
                "args(teacher,datePeriod)"
    )
    fun scheduleConsultationDeletion(teacher: Teacher, datePeriod: DatePeriod) {
        val instant = datePeriod.endTime.atZone(ZoneId.of("Europe/Moscow")).toInstant()
        scheduler.schedule({
            val freshDatePeriod =
                teacherRepository.findById(teacher.id!!).orElseThrow().datePeriods
                    .stream()
                    .filter { teacherDatePeriod: DatePeriod ->
                        teacherDatePeriod.startTime.isEqual(datePeriod.startTime) &&
                                teacherDatePeriod.endTime.isEqual(datePeriod.endTime)
                    }
                    .findAny()
            if (freshDatePeriod.isPresent && freshDatePeriod.get().endTime.isEqual(datePeriod.endTime)) consultationMaker.deleteConsultation(
                teacherRepository.findById(teacher.id!!).orElseThrow(), freshDatePeriod.get()
            )
        }, instant)
    }
}