package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker;
import dev.lampirg.consultationappointment.web.fetch.ConsultationInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    ConsultationMaker consultationMaker;
    TeacherRepository teacherRepository;

    public TeacherController(ConsultationMaker consultationMaker, TeacherRepository teacherRepository) {
        this.consultationMaker = consultationMaker;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/profile")
    public String getTeacherProfile(@AuthenticationPrincipal Teacher teacher, Model model) {
        teacher = teacherRepository.findById(teacher.getId()).orElseThrow();
        model.addAttribute("teacher", teacher);
        List<DatePeriod> datePeriods = new ArrayList<>(teacher.getDatePeriods());
        datePeriods.sort(Comparator.comparing(DatePeriod::getStartTime));
        model.addAttribute("consultations", datePeriods);
        return "teacher/teacher-profile";
    }

    @PostMapping("/{consultationId}/delete")
    public String deleteConsultation(@PathVariable Long consultationId, @AuthenticationPrincipal Teacher teacher) {
        DatePeriod datePeriod = teacherRepository.findDatePeriodById(consultationId).orElseThrow();
        consultationMaker.deleteConsultation(teacherRepository.findById(teacher.getId()).orElseThrow(), datePeriod);
        return "redirect:/teacher/profile";
    }

    @GetMapping("/consultation/add")
    public String openConsultationCreationPage(Model model) {
        model.addAttribute("consultation", new ConsultationInfo());
        return "teacher/create-consultation";
    }

    @PostMapping("/consultation/add")
    public String createConsultation(ConsultationInfo consultation, @AuthenticationPrincipal Teacher teacher) {
        DatePeriod datePeriod = new DatePeriod(
                consultation.getClassroom(),
                LocalDateTime.of(consultation.getDate(), consultation.getStartTime()),
                LocalDateTime.of(consultation.getDate(), consultation.getEndTime())
        );
        consultationMaker.createConsultation(teacherRepository.findById(teacher.getId()).orElseThrow(), datePeriod);
        return "redirect:/teacher/profile";
    }

    // TODO: methods for creating, displaying and deleting consultation patterns ("/pattern/add" and "/pattern")
    }

}
