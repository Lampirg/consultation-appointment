package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    ConsultationMaker consultationMaker;

    public TeacherController(ConsultationMaker consultationMaker) {
        this.consultationMaker = consultationMaker;
    }

    @GetMapping("/profile")
    public String getTeacherProfile(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("teacher", teacher);
        List<DatePeriod> datePeriods = new ArrayList<>(teacher.getDatePeriods());
        datePeriods.sort(Comparator.comparing(DatePeriod::getStartTime));
        model.addAttribute("consultations", datePeriods);
        return "teacher/teacher-profile";
    }

    @PostMapping("/{consultationId}/delete")
    public String deleteConsultation(@PathVariable Long consultationId, @AuthenticationPrincipal Teacher teacher) {
        consultationMaker.deleteConsultationById(teacher, consultationId);
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
                consultation.classroom,
                LocalDateTime.of(consultation.date, consultation.startTime),
                LocalDateTime.of(consultation.date, consultation.endTime)
        );
        consultationMaker.createConsultation(teacher, datePeriod);
        return "redirect:/teacher/profile";
    }

    // TODO: methods for creating, displaying and deleting consultation patterns ("/pattern/add" and "/pattern")
    // TODO: delete consultations which time has passed

    @Data
    public static class ConsultationInfo {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate date;

        private String classroom;
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime startTime;
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        private LocalTime endTime;

    }

}
