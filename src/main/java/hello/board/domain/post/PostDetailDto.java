package hello.board.domain.post;

import hello.board.domain.TimeUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailDto {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String title;
    private final String content;
    private final String createDateTime;
    private final List<ImageDto> images;

    public PostDetailDto(Long id, String username, String nickname, String title, String content, LocalDateTime createDateTime, List<ImageDto> images) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDateTime = TimeUtil.getTime(createDateTime);
        this.images = images;
    }
}
