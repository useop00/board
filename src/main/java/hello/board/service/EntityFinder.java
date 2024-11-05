package hello.board.service;

import hello.board.domain.comment.Comment;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("댓글을 찾을 수 없습니다."));
    }

    public User getLoginUser(String loginId) {
        return userRepository.findByUsername(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
