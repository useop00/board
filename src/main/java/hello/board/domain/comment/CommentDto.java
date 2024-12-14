package hello.board.domain.comment;

import hello.board.domain.TimeUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private final Long id;
    private final Long postId;
    private final String content;
    private final LocalDateTime createdDateTime;
    private final String nickname;
    private final String username;
    private final String createTime;

    @Builder
    private CommentDto(Long id, Long postId, String content, LocalDateTime createdDate, String nickname, String username) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.createdDateTime = createdDate;
        this.createTime = TimeUtil.getTime(createdDateTime);
        this.nickname = nickname;
        this.username = username;
    }

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdDate(comment.getCreateDateTime())
                .nickname(comment.getUser().getNickname())
                .username(comment.getUser().getUsername())
                .build();
    }
}