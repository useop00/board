package hello.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {

    private String content;

    private Long userId;
}
