package hello.board.domain.post;

import com.querydsl.core.annotations.QueryProjection;
import hello.board.domain.TimeUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MainPostDto {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String title;
    private final String content;
    private final String createDateTime;

    @QueryProjection
    public MainPostDto(Long id, String username, String nickname, String title, String content, LocalDateTime createDateTime) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDateTime = TimeUtil.getTime(createDateTime);
    }
}
