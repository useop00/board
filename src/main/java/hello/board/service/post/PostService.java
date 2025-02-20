package hello.board.service.post;

import hello.board.domain.entity.post.Post;
import hello.board.domain.entity.post.PostStatistics;
import hello.board.domain.entity.post.image.Image;
import hello.board.domain.entity.user.User;
import hello.board.domain.repository.PostQueryRepository;
import hello.board.domain.repository.PostRepository;
import hello.board.domain.repository.PostStatisticsRepository;
import hello.board.exception.ImageException;
import hello.board.service.EntityFinder;
import hello.board.service.image.ImageService;
import hello.board.service.post.dto.*;
import hello.board.service.post.poststatistics.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageService imageService;
    private final EntityFinder entityFinder;
    private final PostQueryRepository postQueryRepository;
    private final PostStatisticsRepository postStatisticsRepository;
    private final ViewService viewService;

    @Transactional
    public Long savePost(PostForm form, List<MultipartFile> imageFiles, String loginId) {
        User loginUser = entityFinder.getLoginUser(loginId);

        Post post = form.toEntity(loginUser);
        Post savedPost = postRepository.save(post);

        if (isImageHaveBy(imageFiles)) {
            saveImagesToPost(imageFiles, post);
        }

        createPostStatistics(savedPost.getId());

        return savedPost.getId();
    }

    public PostDetailDto findByPostId(Long postId, HttpServletRequest request) {
        Post post = entityFinder.getPost(postId);

        viewService.handleView(postId, request);

        Integer likeCount = post.getLikeCount();
        Integer viewCount = viewService.getViewCount(postId);

        return PostDetailDto.of(post, viewCount,likeCount);
    }

    public PostForm findPostForm(Long postId) {
        Post post = entityFinder.getPost(postId);

        return PostForm.of(post);
    }

    @Transactional
    public void updatePost(Long postId ,PostForm form, List<MultipartFile> imageFiles, String loginId, List<Long> imageIdsToDelete) {
        Post post = entityFinder.getPost(postId);
        User loginUser = entityFinder.getLoginUser(loginId);

        checkAuthorization(post.getUser(), loginUser);

        if (isImageDeleteBy(imageIdsToDelete)) {
            deleteSelectedImages(post, imageIdsToDelete);
        }
        if (isImageHaveBy(imageFiles)) {
            saveImagesToPost(imageFiles, post);
        }
        post.updatePost(form.getTitle(), form.getContent());
    }

    @Transactional
    public void deletePost(Long id, String loginId) {
        Post post = entityFinder.getPost(id);
        User loginUser = entityFinder.getLoginUser(loginId);

        checkAuthorization(post.getUser(), loginUser);

        deleteAllImages(post);
        postRepository.delete(post);
    }


    public Page<MainPostDto> searchPosts(PostSearch search, Pageable page){
        return postQueryRepository.searchPosts(search, page);
    }

    private void createPostStatistics(Long postId) {
        PostStatistics statistics = PostStatistics.builder()
                .postId(String.valueOf(postId))
                .viewCount(0)
                .build();
        postStatisticsRepository.save(statistics);
    }

    private static boolean isImageDeleteBy(List<Long> imageIdsToDelete) {
        return imageIdsToDelete != null && !imageIdsToDelete.isEmpty();
    }

    private static boolean isImageHaveBy(List<MultipartFile> imageFiles) {
        return imageFiles != null && !imageFiles.isEmpty();
    }

    private void saveImagesToPost(List<MultipartFile> imageFiles, Post post) {
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                try {
                    Image image = imageService.saveImage(file);
                    image.setPost(post);
                    post.addImage(image);
                } catch (IOException e) {
                    throw new ImageException("이미지 저장에 실패했습니다.");
                }
            }
        }
    }

    private void deleteSelectedImages(Post post, List<Long> imageIdsToDelete) {
        post.getImages().removeIf(image -> {
            boolean isToDelete = imageIdsToDelete.contains(image.getId());
            if (isToDelete) {
                deleteImageSafely(image.getId());
            }
            return isToDelete;
        });
    }

    private void deleteAllImages(Post post) {
        post.getImages().forEach(image -> deleteImageSafely(image.getId()));
        post.getImages().clear();
    }

    private void deleteImageSafely(Long imageId) {
        try {
            imageService.deleteImage(imageId);
        } catch (IOException e) {
            throw new ImageException("이미지 삭제에 실패했습니다.");
        }
    }

    private void checkAuthorization(User postOwner, User loginUser) {
        if (!postOwner.equals(loginUser)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}