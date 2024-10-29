package hello.board.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostDto {

    private Long id;
    private String title;
    private String content;

    public UpdatePostDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
