package hello.board.service;

import hello.board.domain.post.Image;
import hello.board.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private FileService fileService;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveImage_성공() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("image", "test.jpa", "image/jpeg", "test data".getBytes());

        String imgName = "fileName.jpg";
        String imgUrl = "http://test.com/fileName.jpg";

        when(fileService.uploadFile(anyString(), any())).thenReturn(imgName);
        when(fileService.generateFileUrl(imgName)).thenReturn(imgUrl);

        Image expectedImage = Image.builder()
                .imgName(imgName)
                .oriImgName(file.getOriginalFilename())
                .imgUrl(imgUrl)
                .build();

        when(imageRepository.save(any(Image.class))).thenReturn(expectedImage);

        //when
        Image savedImage = imageService.saveImage(file);

        //then
        assertEquals(expectedImage.getImgName(), savedImage.getImgName());
        assertEquals(expectedImage.getImgUrl(), savedImage.getImgUrl());
        assertEquals(expectedImage.getOriImgName(), savedImage.getOriImgName());

        // verity
        verify(fileService, times(1)).uploadFile(anyString(), any());
        verify(fileService, times(1)).generateFileUrl(imgName);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

}