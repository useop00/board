package hello.board.service.image;


import hello.board.domain.post.Image;
import hello.board.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final FileService fileService;
    private final ImageRepository imageRepository;


    public Image saveImage(MultipartFile file) throws IOException {
        String oriImgName = file.getOriginalFilename();
        String imgName = fileService.uploadFile(oriImgName, file.getBytes());
        String imgUrl = fileService.generateFileUrl(imgName);

        Image image = Image.builder()
                .imgName(imgName)
                .oriImgName(file.getOriginalFilename())
                .imgUrl(imgUrl)
                .build();

        return imageRepository.save(image);
    }

    public void deleteImage(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다."));

        fileService.deleteFile(image.getImgName());
        imageRepository.delete(image);
    }

}
