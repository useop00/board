package hello.board.service;

import hello.board.domain.Comment;
import hello.board.domain.Post;
import hello.board.domain.User;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment createComment(String content, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Comment comment = new Comment(content, post, user);
        return commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        comment.changeContent(content);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        commentRepository.delete(comment);
    }

    // 특정 포스트에 속한 모든 댓글 조회
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
