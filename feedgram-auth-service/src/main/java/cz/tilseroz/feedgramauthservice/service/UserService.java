package cz.tilseroz.feedgramauthservice.service;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.enums.RoleEnum;
import cz.tilseroz.feedgramauthservice.exception.EmailAlreadyUsedException;
import cz.tilseroz.feedgramauthservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramauthservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public User registerUser(User user) {
        log.info("Request for registration of user {}", user.getUsername());

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Username {} is already used.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("Username %s already used", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Email address {} is alredy used.", user.getEmail());

            throw new EmailAlreadyUsedException(
                    String.format("Email address %s is already used.", user.getEmail()));
        }

        log.info("Registering user {} with email address {}.", user.getUsername(), user.getEmail());

        user.setActive(true);
        user.setRole(RoleEnum.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

}
