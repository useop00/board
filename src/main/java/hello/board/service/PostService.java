package hello.board.service;

import hello.board.domain.post.*;
import hello.board.domain.user.User;
import hello.board.repository.PostQueryRepository;
import hello.board.repository.PostRepository;
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

        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .user(loginUser)
                .build();

        Post savedPost = postRepository.save(post);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            images(imageFiles, savedPost);
        }

        return savedPost.getId();
    }
    public Post findByPostId(Long postId) {
        return entityFinder.getPost(postId);
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByPostDateDesc();
    }

    public PostForm findPostForm(Long postId) {
        Post post = entityFinder.getPost(postId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        List<ImageDto> existingImages = post.getImages().stream()
                .map(image -> new ImageDto(image.getId(), image.getImgUrl()))
                .toList();
        form.setExistingImages(existingImages);

        return form;
    }

    @Transactional
    public void updatePost(UpdateForm form, List<MultipartFile> imageFiles, String loginId, List<Long> imageIdsToDelete) {
        Post post = entityFinder.getPost(form.getId());
        User loginUser = entityFinder.getLoginUser(loginId);

        if (!post.getUser().equals(loginUser)) {
            throw getArgumentException();
        }

        // 선택된 이미지만 삭제
        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            List<Image> existingImages = post.getImages();
            for (Long imageId : imageIdsToDelete) {
                existingImages.stream()
                        .filter(image -> image.getId().equals(imageId))
                        .findFirst()
                        .ifPresent(image -> {
                            try {
                                imageService.deleteImage(imageId);
                                post.getImages().remove(image);
                            } catch (IOException e) {
                                throw getRuntimeException(e);
                            }
                        });
            }
        }
        // 새로운 이미지 추가
        if (imageFiles != null && !imageFiles.isEmpty()) {
            images(imageFiles, post);
        }

        post.updatePost(form.getTitle(), form.getContent());
    }

    @Transactional
    public void deletePost(Long id, String loginId) {
        Post post = entityFinder.getPost(id);
        User loginUser = entityFinder.getLoginUser(loginId);

        if (!post.getUser().equals(loginUser)) {
            throw getArgumentException();
        }
        List<Image> images = post.getImages();
        for (Image image : images) {
            try {
                imageService.deleteImage(image.getId());
            } catch (IOException e) {
                throw getRuntimeException(e);
            }
        }

        postRepository.delete(post);
    }

    public Page<MainPostDto> searchPosts(PostSearch search, Pageable page){
        return postQueryRepository.searchPosts(search, page);
    }

    private void images(List<MultipartFile> imageFiles, Post post) {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    try {
                        Image image = imageService.saveImage(file);
                        post.addImage(image);
                    } catch (IOException e) {
                        throw new RuntimeException("이미지 저장에 실패했습니다.", e);
                    }
                }
            }
        }
    }

    private static IllegalArgumentException getArgumentException() {
        return new IllegalArgumentException("권한이 없습니다.");
    }

    private static RuntimeException getRuntimeException(IOException e) {
        return new RuntimeException("이미지 삭제에 실패했습니다.", e);
    }
}