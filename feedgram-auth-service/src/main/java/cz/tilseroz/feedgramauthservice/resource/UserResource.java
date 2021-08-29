package cz.tilseroz.feedgramauthservice.resource;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.exception.BadRequestException;
import cz.tilseroz.feedgramauthservice.exception.EmailAlreadyUsedException;
import cz.tilseroz.feedgramauthservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramauthservice.payload.ApiResponse;
import cz.tilseroz.feedgramauthservice.payload.JwtAuthenticationResponse;
import cz.tilseroz.feedgramauthservice.payload.LoginRequest;
import cz.tilseroz.feedgramauthservice.payload.SignUpRequest;
import cz.tilseroz.feedgramauthservice.service.JwtProvider;
import cz.tilseroz.feedgramauthservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
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
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
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

}
