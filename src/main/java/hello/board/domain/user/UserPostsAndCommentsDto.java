package hello.board.domain.user;

import hello.board.domain.comment.CommentDto;
import hello.board.domain.post.MyPagePostDto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserPostsAndCommentsDto {

    private List<MyPagePostDto> posts;
    private List<CommentDto> comments;

    public UserPostsAndCommentsDto(List<MyPagePostDto> posts, List<CommentDto> comments) {
        this.posts = posts;
        this.comments = comments;
    }
}
