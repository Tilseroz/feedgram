package cz.tilseroz.feedgramauthservice.configuration;

import cz.tilseroz.feedgramauthservice.model.FeedUserDetails;
import cz.tilseroz.feedgramauthservice.service.JwtProvider;
import cz.tilseroz.feedgramauthservice.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Základní třída filtu - OncePerRequestFiler - nám zaručí, že se filter provede pouze jednou pro jeden request.
 * Autentikaci chceme provádět vždycky jenom jednou v pro jeden request.
 * Může se stát, že nám přijde jiný request, který ale používá stejný filter a tím dojde k provedení requestu znovu.
 * Ve Spring security je to common use-case. Viz https://stackoverflow.com/questions/13152946/what-is-onceperrequestfilter
 *
 * @author David Tilšer
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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
        String token = header.replace(jwtConfig.getPrefix(), "").trim();

        if (tokenProvider.validateJwtToken(token)) {
            Claims claims = tokenProvider.getClaimsFromJwt(token);
            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken authentication;

            /**
             * Pokud je to service account, tak nechceme načítat user detail
             */
            if (username.equals(serviceUsername)) {
                List<String> authorities = (List<String>) claims.get("authorities");
                authentication = new UsernamePasswordAuthenticationToken(username, null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            } else {
                authentication = userService
                        .findByUsername(username)
                        .map(FeedUserDetails::new)
                        .map(userDetails -> {

                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            return authenticationToken;

                        })
                        .orElse(null);
            }

            /**
             * SecurityContext a SecurityContextHolder jsou dvě základní třídy pro Spring Security.
             * SecurityContext se používá pro uchovávání detailů ohledně právě přihlášeného uživatele neboli principle.
             * SecurityContextHolder je helper class. Poskytuje nám přístup k SecurityContextu.
             *
             * Více zde - https://www.javacodegeeks.com/2018/02/securitycontext-securitycontextholder-spring-security.html
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            SecurityContextHolder.clearContext();
        }

        /**
         * Všechno hotové, pojďme do dalšího filtru na řetězci
         */
        filterChain.doFilter(request, response);

    }
}
