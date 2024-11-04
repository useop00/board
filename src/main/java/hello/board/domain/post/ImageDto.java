package hello.board.domain.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {

    private Long id;
    private String imgUrl;

    public ImageDto(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }
}
