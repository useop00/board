package hello.board.service.user;

import hello.board.domain.Role;
import hello.board.domain.comment.CommentDto;
import hello.board.domain.post.MyPagePostDto;
import hello.board.domain.user.User;
import hello.board.domain.user.UserForm;
import hello.board.domain.user.UserPostsAndCommentsDto;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import hello.board.service.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public Long saveUser(UserForm form) {

        if (userRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User savedUser = User.builder()
                .username(form.getUsername())
                .nickname(form.getNickname())
                .password(bCryptPasswordEncoder.encode(form.getPassword()))
                .grade(Role.USER)
                .build();

        userRepository.save(savedUser);
        return savedUser.getId();
    }

    public UserPostsAndCommentsDto getUserPostsAndComments(String username) {
        User user = entityFinder.getLoginUser(username);

        List<MyPagePostDto> posts = getMyPagePostDto(user);

        List<CommentDto> comments = getCommentDto(user);

        return new UserPostsAndCommentsDto(posts, comments);
    }

    private List<MyPagePostDto> getMyPagePostDto(User user) {
        return postRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(post -> new MyPagePostDto(post.getId(), post.getTitle(), post.getCreateDateTime()))
                .toList();
    }

    private List<CommentDto> getCommentDto(User user) {
        return commentRepository.findByUserOrderByCreateDateTimeDesc(user).stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        comment.getCreateDateTime()))
                .toList();
    }
}
