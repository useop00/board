package hello.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EntityFinder entityFinder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return entityFinder.getLoginUser(username);
    }
}

