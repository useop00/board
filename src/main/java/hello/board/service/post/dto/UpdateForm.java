package hello.board.service.post.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateForm {

    private Long id;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    public UpdateForm(PostForm form) {
        this.id = form.getId();
        this.title = form.getTitle();
        this.content = form.getContent();
    }
}
