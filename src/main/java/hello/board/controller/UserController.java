package hello.board.controller;

import hello.board.domain.User;
import hello.board.domain.UserForm;
import hello.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/createUserForm";
    }

    @PostMapping("/users/new")
    public String create(@Validated UserForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "users/createUserForm";
        }
        userService.createUser(form.getUsername(), form.getPassword());
        return "redirect:/";
    }
}
