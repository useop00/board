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
    private final String postDate;

    @QueryProjection
    public MainPostDto(Long id, String username, String nickname, String title, String content, LocalDateTime postDate) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.postDate = TimeUtil.getTime(postDate);
    }
}