package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import dev.lampirg.consultationappointment.service.teacher.ConsultationMaker;
import dev.lampirg.consultationappointment.service.teacher.ConsultationScheduler;
import dev.lampirg.consultationappointment.web.fetch.ConsultationInfo;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPatternListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ConsultationMaker consultationMaker;
    private TeacherRepository teacherRepository;
    private ConsultationScheduler consultationScheduler;
    private ConsultationPatternListWrapper wrapper;

    public TeacherController(ConsultationMaker consultationMaker, TeacherRepository teacherRepository, ConsultationScheduler consultationScheduler) {
        this.consultationMaker = consultationMaker;
        this.teacherRepository = teacherRepository;
        this.consultationScheduler = consultationScheduler;
    }

    @Autowired
    public void setWrapper(ConsultationPatternListWrapper wrapper) {
        this.wrapper = wrapper;
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

    @GetMapping("/pattern")
    public String openPatternPage(Model model, @AuthenticationPrincipal Teacher teacher) {
        wrapper.setPatterns(consultationScheduler.getTeacherPatterns(teacher).orElse(List.of()));
        model.addAttribute("form", wrapper);
        return "teacher/patterns";
    }

    @PostMapping("/pattern/delete")
    public String deletePattern(Integer index) {
        consultationScheduler.removePattern(wrapper.getPatterns().get(index));
        return "redirect:/teacher/pattern";
    }

    @GetMapping("/pattern/add")
    public String openPatternCreationPage(Model model) {
        model.addAttribute("pattern", new ConsultationPattern());
        return "teacher/create-pattern";
    }

    @PostMapping("/pattern/add")
    public String addPattern(ConsultationPattern pattern, @AuthenticationPrincipal Teacher teacher) {
        pattern.setTeacher(teacherRepository.findById(teacher.getId()).orElseThrow());
        pattern.setFromDate(pattern.getConsultationInfo().getDate());
        consultationScheduler.addPattern(pattern);
        consultationScheduler.savePattern(pattern);
        return "redirect:/teacher/profile";
    }

}
