package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.student.AppointmentMaker;
import dev.lampirg.consultationappointment.service.student.SimpleAppointmentMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private TeacherRepository teacherRepository;
    private AppointmentMaker appointmentMaker;
    private StudentRepository studentRepository;

    public StudentController(TeacherRepository teacherRepository, AppointmentMaker appointmentMaker, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.appointmentMaker = appointmentMaker;
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("teacher")
    public Teacher findTeacher(@PathVariable(name = "id", required = false) Integer id) {
        return id == null ? new Teacher() : teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/profile")
    public String getStudentProfile(@AuthenticationPrincipal Student student, Model model) {
        student = studentRepository.findById(student.getId()).orElseThrow();
        model.addAttribute("student", student);
        List<Appointment> appointments = new ArrayList<>(student.getAppointment());
        appointments.sort(Comparator.comparing(Appointment::getStartTime));
        model.addAttribute("appointments", appointments);
        return "student/student-profile";
    }

    @GetMapping("/teachers/{id}")
    public ModelAndView openTeacherPage(@PathVariable("id") int id, @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("student/teacher-details");
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        modelAndView.addObject(teacher);
        Set<Appointment> appointments = new HashSet<>(studentRepository.findById(student.getId()).orElseThrow().getAppointment());
        appointments.retainAll(teacher.getAppointment());
        List<Appointment> appointmentList = new ArrayList<>(appointments);
        appointmentList.sort(Comparator.comparing(Appointment::getStartTime));
        modelAndView.addObject("appointments", appointmentList);
        return modelAndView;
    }

    @GetMapping("/teachers/{id}/add")
    public ModelAndView openAddConsultationPage(@PathVariable("id") int id,
                                                @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("student/add-consultation");
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        List<DatePeriod> datePeriods = teacher.getDatePeriods().stream()
                .filter(datePeriod -> appointmentMaker.isAvailable(teacher, studentRepository.findById(student.getId()).orElseThrow(), datePeriod))
                .sorted(Comparator.comparing(DatePeriod::getStartTime))
                .collect(Collectors.toList());
        modelAndView.addObject("datePeriods", datePeriods);
        modelAndView.addObject(teacher);
        return modelAndView;
    }

    @PostMapping("/teachers/{id}/add")
    public String addConsultation(@PathVariable("id") int id, Long datePeriodId,
                                  Teacher teacher, @AuthenticationPrincipal Student student) {
        DatePeriod datePeriod = teacherRepository.findDatePeriodById(datePeriodId).orElseThrow();
        appointmentMaker.makeAppointment(teacher, studentRepository.findById(student.getId()).orElseThrow(), datePeriod);
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
        model.addAttribute("teachers", teacherRepository.findAll());
        return "student/teachers-list";
    }

    @GetMapping("/teachers")
    public String processFindForm() {
        return "redirect:/student/teachers/find";
    }
}
