package dev.lampirg.consultationappointment.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @GetMapping("/student/login")
    public String studentLogin(Model model, HttpServletRequest request, @RequestParam(defaultValue = "false") boolean hasError) {
        if (hasError)
            getErrorMessage(request).ifPresent((message) -> model.addAttribute("error", message));
        model.addAttribute("login", "/student/login");
        return "login";
    }

    @GetMapping("/teacher/login")
    public String teacherLogin(Model model) {
        model.addAttribute("login", "/teacher/login");
        return "login";
    }

    private Optional<String> getErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Optional.empty();
        }
        AuthenticationException ex = (AuthenticationException) session
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (ex == null) {
            return Optional.empty();
        }
        return Optional.of(ex.getMessage());
    }
}
