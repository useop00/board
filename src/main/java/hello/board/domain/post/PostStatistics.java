package hello.board.domain.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("PostStatistics")
public class PostStatistics implements Serializable {

    @Id
    private String postId;
    private Integer viewCount;

    @Builder
    private PostStatistics(String postId,Integer viewCount) {
        this.postId = postId;
        this.viewCount = viewCount;
    }

    public void increaseViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }
}
