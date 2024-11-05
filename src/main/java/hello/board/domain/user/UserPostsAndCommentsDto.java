package hello.board.domain.user;

import hello.board.domain.comment.CommentDto;
import hello.board.domain.post.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserPostsAndCommentsDto {

    private List<PostDto> posts;
    private List<CommentDto> comments;

    public UserPostsAndCommentsDto(List<PostDto> posts, List<CommentDto> comments) {
        this.posts = posts;
        this.comments = comments;
    }
}
