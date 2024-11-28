package hello.board.service.comment;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentDto;
import hello.board.domain.comment.CommentForm;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.CommentRepository;
import hello.board.service.EntityFinder;
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

        Comment comment = form.toEntity(loginUser, post);

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAll(Long postId) {
        Post post = entityFinder.getPost(postId);

        return getCommentDto(post);
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

    private static List<CommentDto> getCommentDto(Post post) {
        return post.getComments().stream()
                .map(CommentDto::of)
                .toList();
    }

    private static void checkAuthorization(Comment findComment, User user) {
        if (!findComment.getUser().equals(user)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
