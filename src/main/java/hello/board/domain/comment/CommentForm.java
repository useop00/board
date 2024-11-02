package hello.board.domain.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    @NotEmpty(message = "댓글을 작성해주세요.")
    private String content;

    private Long postId;
}
