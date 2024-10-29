package hello.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime commentDate;

    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.commentDate = LocalDateTime.now();
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
