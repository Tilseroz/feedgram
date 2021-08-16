package cz.tilseroz.feedgramauthservice.service;

import cz.tilseroz.feedgramauthservice.configuration.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtProvider {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    /**
     * Generating of token, good page for understading of claims https://datatracker.ietf.org/doc/html/rfc7519#section-4.1
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {

        Long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + jwtConfiguration.getExpiration() * 1000)) //in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSecret().getBytes())
                .compact();
    }

    public Claims getClaimsFromJwt(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(jwtConfiguration.getSecret().getBytes())
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtConfiguration.getSecret().getBytes())
                    .parseClaimsJws(jwtToken);

            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
