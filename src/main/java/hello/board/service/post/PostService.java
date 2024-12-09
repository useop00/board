package hello.board.service.post;

import hello.board.domain.post.*;
import hello.board.domain.user.User;
import hello.board.repository.PostQueryRepository;
import hello.board.repository.PostRepository;
import hello.board.service.EntityFinder;
import hello.board.service.image.ImageService;
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

    @Transactional
    public Long savePost(PostForm form, List<MultipartFile> imageFiles, String loginId) {
        User loginUser = entityFinder.getLoginUser(loginId);

        Post post = form.toEntity(loginUser);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            saveImagesToPost(imageFiles, post);
        }

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }
    public PostDetailDto findByPostId(Long postId) {
        Post post = entityFinder.getPost(postId);

        return PostDetailDto.of(post);
    }

    public PostForm findPostForm(Long postId) {
        Post post = entityFinder.getPost(postId);

        return PostForm.of(post);
    }

    @Transactional
    public void updatePost(UpdateForm form, List<MultipartFile> imageFiles, String loginId, List<Long> imageIdsToDelete) {
        Post post = entityFinder.getPost(form.getId());
        User loginUser = entityFinder.getLoginUser(loginId);

        checkAuthorization(post.getUser(), loginUser);

        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            deleteSelectedImages(post, imageIdsToDelete);
        }
        if (imageFiles != null && !imageFiles.isEmpty()) {
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


    private void saveImagesToPost(List<MultipartFile> imageFiles, Post post) {
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                try {
                    Image image = imageService.saveImage(file);
                    image.setPost(post);
                    post.addImage(image);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장에 실패했습니다.", e);
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
            throw new RuntimeException("이미지 삭제에 실패했습니다.", e);
        }
    }

    private void checkAuthorization(User postOwner, User loginUser) {
        if (!postOwner.equals(loginUser)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}