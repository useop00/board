package hello.board.controller;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentForm;
import hello.board.domain.post.PostForm;
import hello.board.domain.post.UpdateForm;
import hello.board.service.CommentService;
import hello.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/")
    public String postList(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts/postList";
    }

    @GetMapping("/post/create")
    public String createPostForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "posts/createPostForm";
    }

    @PostMapping("/post/create")
    public String createPost(@Validated @ModelAttribute("form") PostForm form,
                             BindingResult result, Principal pri, RedirectAttributes redirect,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        if (result.hasErrors()) {
            return "posts/createPostForm";
        }
        Long postId = postService.savePost(form, images, pri.getName());
        redirect.addFlashAttribute("successMessage", "게시글이 작성되었습니다.");
        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        List<Comment> comments = commentService.findAll(postId);
        model.addAttribute("post", postService.findByPostId(postId));
        model.addAttribute("comments", comments);
        model.addAttribute("commentForm", new CommentForm());
        return "posts/viewPost";
    }

    @GetMapping("/post/edit/{postId}")
    public String updatePostForm(@PathVariable Long postId, Model model) {

        PostForm form = postService.findPostForm(postId);
        model.addAttribute("form", form);
        model.addAttribute("postId", postId);
        return "posts/updatePostForm";
    }

    @PostMapping("/post/edit/{postId}")
    public String updatePost(@PathVariable Long postId, @Validated @ModelAttribute("form") PostForm form,
                             @RequestParam(value = "images", required = false) List<MultipartFile> images,
                             @RequestParam(value = "imageIdsToDelete", required = false) List<Long> imageIdsToDelete,
                             Principal pri) {
        form.setId(postId);
        postService.updatePost(new UpdateForm(form), images, pri.getName(), imageIdsToDelete);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, Authentication auth) {
        postService.deletePost(postId, auth.getName());
        return "redirect:/";
    }
}