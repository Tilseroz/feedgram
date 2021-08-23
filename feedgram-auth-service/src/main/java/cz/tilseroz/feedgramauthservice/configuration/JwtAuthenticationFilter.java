package cz.tilseroz.feedgramauthservice.configuration;

import cz.tilseroz.feedgramauthservice.service.JwtProvider;
import cz.tilseroz.feedgramauthservice.service.UserService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    TODO
//    https://stackoverflow.com/questions/13152946/what-is-onceperrequestfilter

    private final JwtConfiguration jwtConfig;
    private JwtProvider tokenProvider;
    private UserService userService;
    private String serviceUsername;

    public JwtAuthenticationFilter(
            String serviceUsername,
            JwtConfiguration jwtConfig,
            JwtProvider tokenProvider,
            UserService userService) {

        this.serviceUsername = serviceUsername;
        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
