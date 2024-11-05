package hello.board.controller;

import hello.board.domain.comment.CommentForm;
import hello.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createComment(@PathVariable Long postId,
                                @Validated @ModelAttribute("commentForm") CommentForm commentForm, BindingResult result,
                                Principal pri, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 작성 중 오류가 발생했습니다.");
            return "redirect:/post/" + postId;
        }
        commentService.saveComment(commentForm, pri.getName());
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 작성되었습니다.");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/edit/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestParam("content") String content,
                                Principal pri, RedirectAttributes redirectAttributes) {
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 내용은 비어있을 수 없습니다.");
            return "redirect:/post/" + postId;
        }

        commentService.updateComment(content, commentId, pri.getName());
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 수정되었습니다.");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/delete/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                Principal pri, RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteComment(commentId, pri.getName());
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/post/" + postId;
    }

}
