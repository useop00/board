package hello.board.domain.post;

import hello.board.domain.TimeUtil;
import lombok.Builder;
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
    private final Integer viewCount;
    private final Integer likeCount;

    @Builder
    private PostDetailDto(Long id, String username, String nickname, String title, String content, LocalDateTime createDateTime, List<ImageDto> images, Integer viewCount, Integer likeCount) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createDateTime = TimeUtil.getTime(createDateTime);
        this.images = images;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public static PostDetailDto of(Post post, Integer viewCount, Integer likeCount) {
        return PostDetailDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .createDateTime(post.getCreateDateTime())
                .images(post.getImages().stream().map(ImageDto::of).toList())
                .viewCount(viewCount)
                .likeCount(likeCount)
                .build();
    }
}
