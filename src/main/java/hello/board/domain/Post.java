package hello.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime postDate;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.postDate = LocalDateTime.now();
    }

    // 제목 변경
    public void changeTitle(String title) {
        this.title = title;
    }

    // 내용 변경
    public void changeContent(String content) {
        this.content = content;
    }

    //팩토리 메서드
    public static Post createPost(String title, String content, User user) {
        return new Post(title, content, user);
    }
}
