package dev.lampirg.consultationappointment.web

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.student.Student
import dev.lampirg.consultationappointment.data.teacher.DatePeriod
import dev.lampirg.consultationappointment.data.teacher.Teacher
import dev.lampirg.consultationappointment.service.student.AppointmentMaker
import dev.lampirg.consultationappointment.service.student.DataForStudent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/student")
class StudentController @Autowired constructor(
    private val dataForStudent: DataForStudent,
    private val appointmentMaker: AppointmentMaker
) {

    @GetMapping("/profile")
    fun getStudentProfile(@AuthenticationPrincipal student: Student, model: Model): String {
        var student = student
        student = dataForStudent.findStudentById(student.id!!)
        model.addAttribute("student", student)
        val appointments: List<Appointment> = ArrayList(student.appointments)
        appointments.sortedWith(Comparator.comparing(Appointment::startTime))
        model.addAttribute("appointments", appointments)
        return "student/student-profile"
    }

    @GetMapping("/teachers/{id}")
    fun openTeacherPage(@PathVariable("id") id: Int, @AuthenticationPrincipal student: Student): ModelAndView {
        val modelAndView = ModelAndView("student/teacher-details")
        val teacher = dataForStudent.findTeacherById(id)
        modelAndView.addObject(teacher)
        val appointments: MutableSet<Appointment> = HashSet(
            dataForStudent.findStudentById(
                student.id!!
            ).appointments
        )
        appointments.retainAll(teacher.appointments)
        val appointmentList: List<Appointment> = ArrayList(appointments)
        appointmentList.sortedWith(Comparator.comparing(Appointment::startTime))
        modelAndView.addObject("appointments", appointmentList)
        return modelAndView
    }

    @GetMapping("/teachers/{id}/add")
    fun openAddConsultationPage(
        @PathVariable("id") id: Int,
        @AuthenticationPrincipal student: Student
    ): ModelAndView {
        val modelAndView = ModelAndView("student/add-consultation")
        val teacher = dataForStudent.findTeacherById(id)
        val datePeriods = teacher.datePeriods.stream()
            .filter { datePeriod: DatePeriod? ->
                appointmentMaker.isAvailable(
                    teacher, dataForStudent.findStudentById(
                        student.id!!
                    ),
                    datePeriod!!
                )
            }
            .sorted(Comparator.comparing(DatePeriod::startTime))
            .toList()
        modelAndView.addObject("datePeriods", datePeriods)
        modelAndView.addObject(teacher)
        return modelAndView
    }

    @PostMapping("/teachers/{id}/add")
    fun addConsultation(
        @PathVariable("id") id: Int, datePeriodId: Long,
        @AuthenticationPrincipal student: Student
    ): String {
        val datePeriod = dataForStudent.findDatePeriodById(datePeriodId)
        appointmentMaker.makeAppointment(
            dataForStudent.findTeacherById(id),
            dataForStudent.findStudentById(student.id!!),
            datePeriod
        )
        return "redirect:/student/teachers/$id"
    }

    @PostMapping("/teachers/{teacherId}/delete/{appointmentId}", "/appointments/{appointmentId}/delete")
    fun deleteConsultation(
        @PathVariable appointmentId: Long,
        @PathVariable(required = false) teacherId: Int?,
        @AuthenticationPrincipal student: Student
    ): String {
        appointmentMaker.deleteAppointmentById(appointmentId)
        return if (teacherId == null) "redirect:/student/profile" else "redirect:/student/teachers/$teacherId"
    }

    @GetMapping("/teachers/find")
    fun findTeacher(model: Model): String {
        model.addAttribute("teachers", dataForStudent.findAllTeachers())
        return "student/teachers-list"
    }
}