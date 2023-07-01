package dev.lampirg.consultationappointment.web

import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker
import dev.lampirg.consultationappointment.service.teacher.ConsultationScheduler
import dev.lampirg.consultationappointment.service.teacher.DataForTeacher
import dev.lampirg.consultationappointment.web.fetch.ConsultationInfo
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern
import dev.lampirg.consultationappointment.web.fetch.ConsultationPatternListWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime

@Controller
@RequestMapping("/teacher")
class TeacherController @Autowired constructor(
    private val consultationMaker: ConsultationMaker,
    private val dataForTeacher: DataForTeacher,
    private val consultationScheduler: ConsultationScheduler
) {
    @set:Autowired
    lateinit var wrapper: ConsultationPatternListWrapper

    @GetMapping("/profile")
    fun getTeacherProfile(@AuthenticationPrincipal teacher: Teacher, model: Model): String {
        var teacher = teacher
        teacher = dataForTeacher.findTeacherById(teacher.id!!)
        model.addAttribute("teacher", teacher)
        val datePeriods: MutableList<DatePeriod> = ArrayList(teacher.datePeriods)
        datePeriods.sortWith(Comparator.comparing(DatePeriod::startTime))
        model.addAttribute("consultations", datePeriods)
        return "teacher/teacher-profile"
    }

    @PostMapping("/{consultationId}/delete")
    fun deleteConsultation(@PathVariable consultationId: Long?, @AuthenticationPrincipal teacher: Teacher): String {
        val datePeriod = dataForTeacher.findDatePeriodById(consultationId)
        consultationMaker.deleteConsultation(dataForTeacher.findTeacherById(teacher.id!!), datePeriod)
        return "redirect:/teacher/profile"
    }

    @GetMapping("/consultation/add")
    fun openConsultationCreationPage(model: Model): String {
        model.addAttribute("consultation", ConsultationInfo())
        return "teacher/create-consultation"
    }

    @PostMapping("/consultation/add")
    fun createConsultation(consultation: ConsultationInfo, @AuthenticationPrincipal teacher: Teacher): String {
        val datePeriod = DatePeriod(
            consultation.classroom,
            LocalDateTime.of(consultation.date, consultation.startTime),
            LocalDateTime.of(consultation.date, consultation.endTime)
        )
        consultationMaker.createConsultation(dataForTeacher.findTeacherById(teacher.id!!), datePeriod)
        return "redirect:/teacher/profile"
    }

    @GetMapping("/pattern")
    fun openPatternPage(model: Model, @AuthenticationPrincipal teacher: Teacher?): String {
        wrapper.patterns = consultationScheduler.getTeacherPatterns(teacher!!).orElse(listOf())
        model.addAttribute("form", wrapper)
        return "teacher/patterns"
    }

    @PostMapping("/pattern/delete")
    fun deletePattern(index: Int): String {
        consultationScheduler.removePattern(wrapper.patterns!![index])
        return "redirect:/teacher/pattern"
    }

    @GetMapping("/pattern/add")
    fun openPatternCreationPage(model: Model): String {
        model.addAttribute("pattern", ConsultationPattern())
        return "teacher/create-pattern"
    }

    @PostMapping("/pattern/add")
    fun addPattern(pattern: ConsultationPattern, @AuthenticationPrincipal teacher: Teacher): String {
        pattern.teacher = dataForTeacher.findTeacherById(teacher.id!!)
        pattern.fromDate = pattern.consultationInfo.date
        consultationScheduler.addPattern(pattern)
        consultationScheduler.savePattern(pattern)
        return "redirect:/teacher/profile"
    }
}