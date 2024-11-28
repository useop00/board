package hello.board.domain.comment;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime createdDateTime;

    public CommentDto(Long id, Long postId, String content, LocalDateTime createdDate) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdDateTime = createdDate;
    }
}