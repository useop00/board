package hello.board.domain.post;

import hello.board.domain.TimeUtil;
import hello.board.domain.comment.Comment;
import hello.board.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "post_date")
    private LocalDateTime postDate;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.setUser(user);
        this.postDate = LocalDateTime.now();
    }

    //==연관 관계 메서드==//
    public void setUser(User user) {
        this.user = user;
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getFormattedPostDate() {
        return TimeUtil.getTime(this.postDate);
    }
}
