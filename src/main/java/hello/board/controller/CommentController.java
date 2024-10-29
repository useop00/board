package hello.board.controller;

import hello.board.domain.CommentForm;
import hello.board.service.CommentService;
import hello.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/posts/{postId}/comments/new")
    public String createForm(@PathVariable Long postId, Model model) {
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("postId", postId);
        model.addAttribute("users", userService.findAll());
        return "comments/createCommentForm";
    }

    @PostMapping("/posts/{postId}/comments/new")
    public String createComment(@PathVariable Long postId,
                                @Valid @ModelAttribute CommentForm commentForm,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "comments/createCommentForm";
        }

        commentService.createComment(commentForm.getContent(), postId, commentForm.getUserId());
        redirectAttributes.addFlashAttribute("message", "댓글이 작성되었습니다.");
        return "redirect:/posts/" + postId;
    }

}
