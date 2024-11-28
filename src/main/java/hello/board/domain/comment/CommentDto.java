package hello.board.domain.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime createdDateTime;

    @Builder
    private CommentDto(Long id, Long postId, String content, LocalDateTime createdDate) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdDateTime = createdDate;
    }

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdDate(comment.getCreateDateTime())
                .build();
    }
}