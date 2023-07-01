package dev.lampirg.consultationappointment.web

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.WebAttributes
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
class LoginController {
    @GetMapping("", "/home")
    fun index(): String {
        return "index"
    }

    @GetMapping("/student/login")
    fun studentLogin(
        model: Model,
        request: HttpServletRequest,
        @RequestParam(defaultValue = "false") hasError: Boolean
    ): String {
        formModel(hasError, request, model, "/student/login")
        return "login"
    }

    @GetMapping("/student/logout")
    fun studentLogout(model: Model): String {
        model.addAttribute("logout", "/student/logout")
        return "logout"
    }

    @GetMapping("/teacher/login")
    fun teacherLogin(
        model: Model,
        request: HttpServletRequest,
        @RequestParam(defaultValue = "false") hasError: Boolean
    ): String {
        formModel(hasError, request, model, "/teacher/login")
        return "login"
    }

    @GetMapping("/teacher/logout")
    fun teacherLogout(
        model: Model,
        request: HttpServletRequest?,
        @RequestParam(defaultValue = "false") hasError: Boolean
    ): String {
        model.addAttribute("logout", "/teacher/logout")
        return "logout"
    }

    private fun formModel(hasError: Boolean, request: HttpServletRequest, model: Model, attributeValue: String) {
        if (hasError) getErrorMessage(request).ifPresent { message: String? ->
            model.addAttribute(
                "error",
                message
            )
        }
        model.addAttribute("login", attributeValue)
    }

    private fun getErrorMessage(request: HttpServletRequest): Optional<String> {
        val session = request.getSession(false) ?: return Optional.empty()
        val ex = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) as AuthenticationException
        return if (ex.message == null) Optional.empty() else Optional.of(ex.message!!)
    }
}