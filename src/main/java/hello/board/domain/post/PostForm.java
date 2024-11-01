package hello.board.domain.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {

    private Long id;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

}
