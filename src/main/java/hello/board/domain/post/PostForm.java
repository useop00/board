package hello.board.domain.post;

import hello.board.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
public class PostForm {

    private Long id;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private List<MultipartFile> images;

    private List<ImageDto> existingImages;

    public Post toEntity(User user) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }

    public static PostForm of(Post post) {
        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());
        form.setExistingImages(post.getImages().stream().map(ImageDto::of).toList());
        return form;
    }
}
