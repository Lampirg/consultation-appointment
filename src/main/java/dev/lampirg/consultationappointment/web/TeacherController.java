package dev.lampirg.consultationappointment.web;

import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class TeacherController {

    TeacherRepository teacherRepository;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public String appointment() {
        return "redirect:/teachers/find";
    }

    @GetMapping("/teachers/find")
    public String findTeacher(Model model) {
        model.addAttribute("teacher", new Teacher());
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
        }
        else if (ownersResults.getTotalElements() == 1) {
            // 1 teacher found
            teacher = ownersResults.iterator().next();
            return "redirect:/teacher/" + teacher.getId();
        }
        else {
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
