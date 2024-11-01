package hello.board.domain.comment;

import hello.board.domain.TimeUtil;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    @Builder
    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.commentDate = LocalDateTime.now();
    }

    public void updateComment(String newContent) {
        changeContent(newContent);
    }

    private void changeContent(String content) {
        this.content = content;
    }

    public String getFormattedCommentDate() {
        return TimeUtil.getTime(this.commentDate);
    }
}
