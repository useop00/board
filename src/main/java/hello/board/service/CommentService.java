package hello.board.service;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentDto;
import hello.board.domain.comment.CommentForm;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    public Long saveComment(CommentForm form, String loginId) {
        User loginUser = entityFinder.getLoginUser(loginId);
        Post post = entityFinder.getPost(form.getPostId());

        Comment comment = Comment.builder()
                .content(form.getContent())
                .user(loginUser)
                .post(post)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(Long postId) {
        Post post = entityFinder.getPost(postId);

        return post.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        comment.getCreateDateTime()
                )).toList();
    }

    public void updateComment(String content, Long commentId, String username) {
        Comment comment = entityFinder.getComment(commentId);
        User user = entityFinder.getLoginUser(username);

        checkAuthorization(comment, user);

        comment.updateComment(content);
    }

    public void deleteComment(Long id, String loginId) {
        Comment findComment = entityFinder.getComment(id);
        User user = entityFinder.getLoginUser(loginId);

        checkAuthorization(findComment, user);

        commentRepository.delete(findComment);
    }

    private static void checkAuthorization(Comment findComment, User user) {
        if (!findComment.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }


}
