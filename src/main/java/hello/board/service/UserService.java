package hello.board.service;

import hello.board.domain.Role;
import hello.board.domain.user.User;
import hello.board.domain.user.UserForm;
import hello.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long saveUser(UserForm form) {

        if (userRepository.findByUsername(form.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다");
        }

        return userRepository.save(User.builder()
                .username(form.getUsername())
                .nickname(form.getNickname())
                .password(bCryptPasswordEncoder.encode(form.getPassword()))
                .grade(Role.USER)
                .build()).getId();
    }
}
