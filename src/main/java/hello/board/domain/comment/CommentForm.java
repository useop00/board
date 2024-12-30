package hello.board.domain.comment;

import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    @NotEmpty(message = "댓글을 작성해주세요.")
    private String content;

    private Long postId;
    private Long parentId;

    public Comment toEntity(User user, Post post, Comment parent) {
        return Comment.builder()
                .content(this.content)
                .user(user)
                .post(post)
                .parent(parent)
                .build();
    }
}
