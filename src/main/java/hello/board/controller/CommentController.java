package hello.board.controller;

import hello.board.domain.comment.CommentForm;
import hello.board.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(@PathVariable Long postId, @Validated @ModelAttribute("commentForm") CommentForm commentForm, BindingResult result, Principal pri) {
        if (result.hasErrors()) {
            return "redirect:/post/" + postId;
        }
        commentService.saveComment(commentForm, pri.getName());
        return "redirect:/post/" + postId;
    }

    @PostMapping("/edit/{commentId}")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                @RequestParam("content") String content, Principal pri) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/post/" + postId;
        }

        commentService.updateComment(content, commentId, pri.getName());
        return "redirect:/post/" + postId;
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Principal pri) {
        commentService.deleteComment(commentId, pri.getName());

        return "redirect:/post/" + postId;
    }

}
