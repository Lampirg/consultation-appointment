package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.service.student.AppointmentMaker;
import dev.lampirg.consultationappointment.service.student.DataForStudent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private DataForStudent dataForStudent;
    private AppointmentMaker appointmentMaker;

    public StudentController(DataForStudent dataForStudent, AppointmentMaker appointmentMaker) {
        this.dataForStudent = dataForStudent;
        this.appointmentMaker = appointmentMaker;
    }

    @GetMapping("/profile")
    public String getStudentProfile(@AuthenticationPrincipal Student student, Model model) {
        student = dataForStudent.findStudentById(student.getId());
        model.addAttribute("student", student);
        List<Appointment> appointments = new ArrayList<>(student.getAppointments());
        appointments.sort(Comparator.comparing(Appointment::getStartTime));
        model.addAttribute("appointments", appointments);
        return "student/student-profile";
    }

    @GetMapping("/teachers/{id}")
    public ModelAndView openTeacherPage(@PathVariable("id") int id, @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("student/teacher-details");
        Teacher teacher = dataForStudent.findTeacherById(id);
        modelAndView.addObject(teacher);
        Set<Appointment> appointments = new HashSet<>(dataForStudent.findStudentById(student.getId()).getAppointments());
        appointments.retainAll(teacher.getAppointments());
        List<Appointment> appointmentList = new ArrayList<>(appointments);
        appointmentList.sort(Comparator.comparing(Appointment::getStartTime));
        modelAndView.addObject("appointments", appointmentList);
        return modelAndView;
    }

    @GetMapping("/teachers/{id}/add")
    public ModelAndView openAddConsultationPage(@PathVariable("id") int id,
                                                @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("student/add-consultation");
        Teacher teacher = dataForStudent.findTeacherById(id);
        List<DatePeriod> datePeriods = teacher.getDatePeriods().stream()
                .filter(datePeriod -> appointmentMaker.isAvailable(teacher, dataForStudent.findStudentById(student.getId()), datePeriod))
                .sorted(Comparator.comparing(DatePeriod::getStartTime))
                .toList();
        modelAndView.addObject("datePeriods", datePeriods);
        modelAndView.addObject(teacher);
        return modelAndView;
    }

    @PostMapping("/teachers/{id}/add")
    public String addConsultation(@PathVariable("id") int id, Long datePeriodId,
                                  @AuthenticationPrincipal Student student) {
        DatePeriod datePeriod = dataForStudent.findDatePeriodById(datePeriodId);
        appointmentMaker.makeAppointment(dataForStudent.findTeacherById(id), dataForStudent.findStudentById(student.getId()), datePeriod);
        return "redirect:/student/teachers/" + id;
    }

    @PostMapping({"/teachers/{teacherId}/delete/{appointmentId}", "/appointments/{appointmentId}/delete"})
    public String deleteConsultation(@PathVariable Long appointmentId, @PathVariable(required = false) Integer teacherId, @AuthenticationPrincipal Student student) {
        appointmentMaker.deleteAppointmentById(appointmentId);
        if (teacherId == null)
            return "redirect:/student/profile";
        return "redirect:/student/teachers/" + teacherId;
    }

    @GetMapping("/teachers/find")
    public String findTeacher(Model model) {
        model.addAttribute("teachers", dataForStudent.findAllTeachers());
        return "student/teachers-list";
    }
}
