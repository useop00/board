package hello.board.service;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentForm;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Long saveComment(CommentForm form, String loginId) {
        User loginUser = userRepository.findByUsername(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return  commentRepository.save(Comment.builder()
                .content(form.getContent())
                .user(loginUser)
                .post(postRepository.findById(form.getPostId())
                        .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")))
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return post.getComments();
    }

    public void updateComment(String content, Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        comment.updateComment(content);
    }

    public void deleteComment(Long id, String loginId) {
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("댓글을 찾을 수 없습니다."));

        if (!findComment.getUser().getUsername().equals(loginId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        commentRepository.delete(findComment);
    }

}
