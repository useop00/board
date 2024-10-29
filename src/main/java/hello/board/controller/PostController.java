package hello.board.controller;

import hello.board.domain.Comment;
import hello.board.domain.Post;
import hello.board.domain.PostForm;
import hello.board.service.CommentService;
import hello.board.service.PostService;
import hello.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @GetMapping("/posts/new")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("users", userService.findAll());
        return "posts/createPostForm";
    }


    @PostMapping("/posts/new")
    public String createPost(@Validated @ModelAttribute PostForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "posts/createPostForm";
        }

        postService.createPost(form.getTitle(), form.getContent(), form.getUserId());
        redirectAttributes.addFlashAttribute("message", "게시글이 생성되었습니다.");
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.findAllPosts();
        model.addAttribute("posts", posts);
        return "posts/postList";
    }

    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Post post = postService.findPostById(postId);
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "posts/viewPost";
    }
}
