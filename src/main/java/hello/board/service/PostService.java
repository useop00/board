package hello.board.service;

import hello.board.domain.Post;
import hello.board.domain.UpdatePostDto;
import hello.board.domain.User;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
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
    public Post createPost(String title, String content, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없음"));
        Post post = new Post(title, content, user);
        return postRepository.save(post);
    }

    @Transactional
    public void updatePost(UpdatePostDto postDto) {
        Post findPost = postRepository.findById(postDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("못 찾음"));
        findPost.changeTitle(postDto.getTitle());
        findPost.changeContent(postDto.getContent());
    }

    // 포스트 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        postRepository.delete(post);
    }

    // 모든 포스트 조회
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    // 특정 포스트 조회
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
    }
}
