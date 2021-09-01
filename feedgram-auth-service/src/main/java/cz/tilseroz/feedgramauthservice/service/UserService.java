package cz.tilseroz.feedgramauthservice.service;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.enums.RoleEnum;
import cz.tilseroz.feedgramauthservice.exception.EmailAlreadyUsedException;
import cz.tilseroz.feedgramauthservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramauthservice.messaging.UserEventSender;
import cz.tilseroz.feedgramauthservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEventSender userEventSender;

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

        User registeredUser = userRepository.save(user);
        userEventSender.sendUserCreated(registeredUser);

        return registeredUser;
    }

    public User updateUser(User user) {
        log.info("Request for updating user {}", user.getUsername());

        /**
         *  Používáme JpaRepository interface, který je implementovaný ve Springu v SimpleJpaRepository. Tam můžeme v metodě "save" vidět, že pokud entita existuje, tak
         *  použijeme merge, jinak persist. Tzn. save je zde správně a něco ve stylu "udpate" apod. tady nenajdeme.
         */
        user.setModifiedAt(Instant.now());
        User updatedUser = userRepository.save(user);
        userEventSender.sendUserUpdated(user);

        return updatedUser;
    }

}
