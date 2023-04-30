package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.AppointmentMaker;
import dev.lampirg.consultationappointment.service.SimpleAppointmentMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasRole('STUDENT')")
public class TeacherController {

    private TeacherRepository teacherRepository;
    private AppointmentMaker appointmentMaker;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository, SimpleAppointmentMaker appointmentMaker) {
        this.teacherRepository = teacherRepository;
        this.appointmentMaker = appointmentMaker;
    }

    @ModelAttribute("teacher")
    public Teacher findTeacher(@PathVariable(name = "id", required = false) Integer id) {
        return id == null ? new Teacher() : teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/teachers/{id}")
    public ModelAndView openTeacherPage(@PathVariable("id") int id, @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("teacher-details");
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        modelAndView.addObject(teacher);
        Set<Appointment> appointments = new HashSet<>(student.getAppointment());
        appointments.retainAll(teacher.getAppointment());
        modelAndView.addObject("appointments", appointments);
        return modelAndView;
    }

    @GetMapping("/teachers/{id}/add")
    public ModelAndView openAddConsultationPage(@PathVariable("id") int id,
                                                @AuthenticationPrincipal Student student) {
        ModelAndView modelAndView = new ModelAndView("add-consultation");
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        Set<DatePeriod> datePeriods = teacher.getDatePeriods().stream()
                .filter(datePeriod -> appointmentMaker.isAvailable(teacher, student, datePeriod))
                .collect(Collectors.toSet());
        modelAndView.addObject("datePeriods", datePeriods);
        modelAndView.addObject(teacher);
        return modelAndView;
    }

    @PostMapping("/teachers/{id}/add")
    public String addConsultation(@PathVariable("id") int id, Long datePeriodId,
                                  Teacher teacher, @AuthenticationPrincipal Student student) {
        DatePeriod datePeriod = teacherRepository.findDatePeriodById(datePeriodId).orElseThrow();
        appointmentMaker.makeAppointment(teacher, student, datePeriod);
        return "redirect:/teachers/" + id;
    }

    @GetMapping
    public String appointment() {
        return "redirect:/teachers/find";
    }

    @GetMapping("/teachers/find")
    public String findTeacher() {
        return "find-teacher";
    }

    @GetMapping("/teachers")
    public String processFindForm(@RequestParam(defaultValue = "1") int page, Teacher teacher,
                                  BindingResult result, Model model) {
        if (teacher.getLastName() == null) {
            teacher.setLastName("");
        }
        // find owners by last name
        Page<Teacher> ownersResults = findTeachersByLastName(teacher.getLastName(), page);
        if (ownersResults.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "Преподаватель не найден");
            return "find-teacher";
        } else if (ownersResults.getTotalElements() == 1) {
            // 1 teacher found
            teacher = ownersResults.iterator().next();
            return "redirect:/teachers/" + teacher.getId();
        } else {
            // multiple owners found
            return addPaginationModel(page, model, ownersResults);
        }
    }

    public Page<Teacher> findTeachersByLastName(String lastName, int page) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return teacherRepository.findByLastNameStartingWith(lastName, pageable);
    }

    private String addPaginationModel(int page, Model model, Page<Teacher> paginated) {
        model.addAttribute("teachers", paginated);
        List<Teacher> teachers = paginated.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("teachers", teachers);
        return "teachers-list";
    }
}
