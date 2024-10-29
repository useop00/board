package hello.board.service;

import hello.board.domain.User;
import hello.board.repository.UserRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String username, String password) {
        User user = new User(username, password);
        return userRepository.save(user);
    }

    private void findByUsername(User user) {
        List<User> findUsers = userRepository.findByUsername(user.getUsername());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
