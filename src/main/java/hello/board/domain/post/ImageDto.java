package hello.board.domain.post;

import lombok.Getter;

@Getter
public class ImageDto {

    private Long id;
    private String imgUrl;

    public ImageDto(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }
}
