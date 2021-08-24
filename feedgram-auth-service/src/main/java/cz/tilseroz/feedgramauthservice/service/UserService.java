package cz.tilseroz.feedgramauthservice.service;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        log.info("Retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

}
