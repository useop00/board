package hello.board.service.post.poststatistics;

import hello.board.domain.like.Like;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import hello.board.repository.LikeRepository;
import hello.board.service.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final EntityFinder entityFinder;

    public int likePost(Long postId, String userId) {
        Post post = entityFinder.getPost(postId);
        User user = entityFinder.getLoginUser(userId);

        Optional<Like> like = likeRepository.findByPostIdAndUsername(postId, userId);

        if (like.isPresent()) {
            // 좋아요 취소
            likeRepository.delete(like.get());
        } else {
            // 좋아요 추가
            Like newLike = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(newLike);
        }
        return likeRepository.countByPostId(postId).intValue();
    }

    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
