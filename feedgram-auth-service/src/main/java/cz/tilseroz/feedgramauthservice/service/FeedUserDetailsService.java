package cz.tilseroz.feedgramauthservice.service;

import cz.tilseroz.feedgramauthservice.model.FeedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementujeme Spring Security UserDetailsService. Flow Spring Security:
 * 1. prvně ověří, že uživatel existuje a je validní (implementace v FeedUserDetails.isValid)
 * 2. jako druhé ověří heslo
 *
 * Vše se děje zde AbstractUserDetailsAuthenticationProvider.authenticate.
 */
@Service
public class FeedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userService.findByUsername(username)
                .map(FeedUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usename was not found."));
    }
}
