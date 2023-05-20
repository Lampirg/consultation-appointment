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

    @GetMapping({"", "/home"})
    public String index() {
        return "index";
    }

    @GetMapping("/student/login")
    public String studentLogin(Model model, HttpServletRequest request, @RequestParam(defaultValue = "false") boolean hasError) {
        formModel(hasError, request, model, "/student/login");
        return "login";
    }

    @GetMapping("/student/logout")
    public String studentLogout(Model model) {
        model.addAttribute("logout", "/student/logout");
        return "logout";
    }

    @GetMapping("/teacher/login")
    public String teacherLogin(Model model, HttpServletRequest request, @RequestParam(defaultValue = "false") boolean hasError) {
        formModel(hasError, request, model, "/teacher/login");
        return "login";
    }

    @GetMapping("/teacher/logout")
    public String teacherLogout(Model model, HttpServletRequest request, @RequestParam(defaultValue = "false") boolean hasError) {
        model.addAttribute("logout", "/teacher/logout");
        return "logout";
    }

    private void formModel(boolean hasError, HttpServletRequest request, Model model, String attributeValue) {
        if (hasError)
            getErrorMessage(request).ifPresent(message -> model.addAttribute("error", message));
        model.addAttribute("login", attributeValue);
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
