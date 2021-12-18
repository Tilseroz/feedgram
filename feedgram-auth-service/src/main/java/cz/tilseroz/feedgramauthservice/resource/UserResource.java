package cz.tilseroz.feedgramauthservice.resource;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.exception.BadRequestException;
import cz.tilseroz.feedgramauthservice.exception.EmailAlreadyUsedException;
import cz.tilseroz.feedgramauthservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramauthservice.payload.ApiResponse;
import cz.tilseroz.feedgramauthservice.payload.JwtAuthenticationResponse;
import cz.tilseroz.feedgramauthservice.payload.LoginRequest;
import cz.tilseroz.feedgramauthservice.payload.SignUpRequest;
import cz.tilseroz.feedgramauthservice.payload.UserUpdateRequest;
import cz.tilseroz.feedgramauthservice.service.JwtProvider;
import cz.tilseroz.feedgramauthservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class UserResource {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Logging in user {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("Auth-service creating user {}.", signUpRequest.getUsername());

        User user = User
                .builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .createdAt(Instant.now())
                .build();

        try {
            userService.registerUser(user);
        } catch (UsernameAlreadyExistsException | EmailAlreadyUsedException e) {
            throw new BadRequestException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully."));

    }

    @PutMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Auth-service updating user {}.", userUpdateRequest.getUsername());

        User user = userService.findByUsername(userUpdateRequest.getUsername())
                .map(User::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username was not found."));

        boolean anyChanges = false;

        if (!userUpdateRequest.getEmail().isBlank()) {
            user.setEmail(userUpdateRequest.getEmail());
            anyChanges = true;
        }
        if (!userUpdateRequest.getName().isBlank()) {
            user.setName(userUpdateRequest.getName());
            anyChanges = true;
        }

        if (anyChanges) {
            userService.updateUser(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(user.getUsername()).toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true, "User updated successfully."));

        } else {
            log.warn("No changes have been provided by the user.");
            throw new BadRequestException("No changes have been provided by the user.");
        }
    }

}
