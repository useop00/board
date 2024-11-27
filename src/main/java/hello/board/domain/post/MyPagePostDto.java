package hello.board.domain.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPagePostDto {
    private Long id;
    private String title;
    private LocalDateTime createdDateTime;

    public MyPagePostDto(Long id, String title, LocalDateTime createdDateTime) {
        this.id = id;
        this.title = title;
        this.createdDateTime = createdDateTime;
    }
}
