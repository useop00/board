package hello.board.controller;

import hello.board.domain.message.MessageDto;
import hello.board.domain.user.UserForm;
import hello.board.domain.user.UserPostsAndCommentsDto;
import hello.board.service.MessageService;
import hello.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/login")
    public String login() {
        return "users/loginForm";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "users/signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("form") UserForm form, Model model) {
        try {
            userService.saveUser(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "users/signup";
        }
        return "redirect:/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, Principal pri) {
        String username = pri.getName();
        UserPostsAndCommentsDto userContent = userService.getUserPostsAndComments(username);
        model.addAttribute("posts", userContent.getPosts());
        model.addAttribute("comments", userContent.getComments());

        // 받은 쪽지 조회
        List<MessageDto> receivedMessages = messageService.getReceivedMessages(username);
        model.addAttribute("receivedMessages", receivedMessages);

        // 보낸 쪽지 조회
        List<MessageDto> sentMessages = messageService.getSentMessages(username);
        model.addAttribute("sentMessages", sentMessages);

        return "users/mypage";
    }
}
