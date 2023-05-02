package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("/profile")
    public String getTeacherProfile(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("teacher", teacher);
        List<DatePeriod> datePeriods = new ArrayList<>(teacher.getDatePeriods());
        datePeriods.sort(Comparator.comparing(DatePeriod::getStartTime));
        model.addAttribute("consultations", datePeriods);
        return "teacher/teacher-profile";
    }


}
