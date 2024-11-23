package hello.board.service;

import hello.board.domain.Role;
import hello.board.domain.comment.CommentDto;
import hello.board.domain.post.PostDto;
import hello.board.domain.user.User;
import hello.board.domain.user.UserForm;
import hello.board.domain.user.UserPostsAndCommentsDto;
import hello.board.repository.CommentRepository;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        List<PostDto> posts = postRepository.findByUserOrderByPostDateDesc(user).stream()
                .map(post -> new PostDto(post.getId(), post.getTitle(), post.getPostDate()))
                .toList();

        List<CommentDto> comments = commentRepository.findByUserOrderByCommentDateDesc(user).stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        comment.getCommentDate()))
                .collect(Collectors.toList());

        return new UserPostsAndCommentsDto(posts, comments);
    }
}
