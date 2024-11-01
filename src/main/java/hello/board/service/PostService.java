package hello.board.service;

import hello.board.domain.post.Post;
import hello.board.domain.post.PostForm;
import hello.board.domain.post.UpdateForm;
import hello.board.domain.user.User;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long savePost(PostForm form, String loginId) {
        User loginUser = userRepository.findByUsername(loginId).get();

        return postRepository.save(Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .user(loginUser)
                .build()).getId();
    }
    public Post findByPostId(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public PostForm findPostForm(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음"));
        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        return form;
    }

    @Transactional
    public void updatePost(UpdateForm form, String loginId) {
        Post findPost = postRepository.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!findPost.getUser().getUsername().equals(loginId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        findPost.updatePost(form.getTitle(), form.getContent());
    }

    @Transactional
    public void deletePost(Long id, String loginId) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!findPost.getUser().getUsername().equals(loginId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        postRepository.delete(findPost);
    }
}
