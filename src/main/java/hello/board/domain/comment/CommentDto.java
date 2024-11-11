package hello.board.domain.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime createdDate;

    public CommentDto(Long id, Long postId, String content, LocalDateTime createdDate) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdDate = createdDate;
    }
}