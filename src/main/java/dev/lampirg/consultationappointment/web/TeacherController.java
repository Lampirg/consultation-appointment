package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("/profile")
    public String getTeacherProfile(@AuthenticationPrincipal Teacher teacher) {
        return "teacher-profile";
    }

}
