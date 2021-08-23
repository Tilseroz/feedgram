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

        /**
         * Prvním krokem je získat authentication header a ověřit, že tam máme token. Tokeny se mají vždy posílat v headeru.
         * Hledáme header "Authorization".
         */
        String header = request.getHeader(jwtConfig.getHeader());

        /**
         * Zvalidujeme header a zkontrolujeme prefix Bearer, že tam je. Pokud není validní, tak rovnou posíláme do dallšího filtru - filterChain.doFilter
         *
         * Pokud uživatel nebude mít token, tak nebude authentizován. Což je v pořádku, může přistupovat na veřejné URL a ptát se na token.
         * Všechny neveřejné endpointy, které vyžadují token, jsou nastaveny v config třídě. Pokud je uživatel bude chtít navštívit, tak nebude authentizovaný a vyhodí se exception.
         */
        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * Pokud jsme se dostali až sem, tak v headeru mám token. Pojďme si ho vytáhnout.
         */
        String token = header.replace(jwtConfig.getPrefix(), "");

        //TODO pokračovat zde
        if (tokenProvider.validateJwtToken(token)) {

        }

    }
}
