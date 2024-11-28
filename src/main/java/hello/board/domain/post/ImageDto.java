package hello.board.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageDto {

    private Long id;
    private String imgUrl;

    @Builder
    private ImageDto(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public static ImageDto of(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imgUrl(image.getImgUrl())
                .build();
    }
}
