package cz.tilseroz.feedgramauthservice.configuration;

import cz.tilseroz.feedgramauthservice.service.JwtProvider;
import cz.tilseroz.feedgramauthservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * {@link EnableWebSecurity} is used for Spring security java configuration. This annotation is usually used with extending of {@link WebSecurityConfigurerAdapter} together.
 * More - https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/config/annotation/web/configuration/EnableWebSecurity.html
 *
 * {@link EnableGlobalMethodSecurity} is for enable/disable pre pro annotations. Here as a parameter is prePost enabled.
 *
 * Velmi dobrý článek na pochopení Spring security frameworku - https://www.toptal.com/spring/spring-security-tutorial
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter {

    /**
     * Anotace Autowired se stará o autowiring - naplní mi field instancí jiné beany, která "žije" v kontejneru, který se jmenuje "application context".
     *
     * Anotace Value mi uvádí defaultní hodnotu proměnné. Pokud je proměnná null, použiju defaultní hodnotu. V našem případě se odkazuji na hodnotu v application.yml souboru.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfiguration jwtConfig;

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Value("${security.service.username}")
    private String serviceUsername;

    @Value("${security.service.password}")
    private String servicePassword;

    /**
     * konfigurace authentication manager tak, aby měl správného providera
     *
     * AuthenticationManager
     * Do Authentication managera můžeme registrovat různé providery a on na základě typu doručí authentication request do správného provideru
     *
     * AuthenticationProvider
     * Zpracovává konkrétní typy ověřování. Jeho rozhraní nabízí jenom dvě funkce: "authenticate" (řeší authentication request)
     * a "supports" (kontroluje, zda provider podporuje typ autentikace)
     *
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * Konfiguruju in-memory authentication provider pro interní komunikaci services
         */
        auth.inMemoryAuthentication()
                .withUser(serviceUsername)
                .password(passwordEncoder().encode(servicePassword))
                .roles();

        /**
         * Nyní ještě nakonfigurujeme databázový authentication provider
         *
         * Spring má UserDetailService interface, který může být overridden, abychom mohli použít naši implementaci pro získání dat z databáze.
         * UserDetailsService objekt je použit auth managerem pro načtení uživatele z databáze.
         */
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }


    /**
     * Má na starosti konfiguraci web security (veřejné URL, soukromé URL, autorizace, atd.)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * Vypneme CSRF
                 * https://en.wikipedia.org/wiki/Cross-site_request_forgery
                 */
                .csrf().disable()
                /**
                 * Nastavíme vytváření session na stateless, aby Spring nedržel session pro každého usera.
                 */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /**
                 * Nastaváme handler na unathorized requests exceptions
                 */
                .exceptionHandling().authenticationEntryPoint((request, response, e) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                /**
                 * Přidávání token authentication filter. Zachytí požadavek (request) a ověří, že je v headeru token a ověří ho.
                 */
                .addFilterBefore(new JwtAuthenticationFilter(serviceUsername, jwtConfig, tokenProvider, userService), UsernamePasswordAuthenticationFilter.class)
                /**
                 *  Zabezpečíme přístup všech services krom /login a /users pro POST metody, protože chceme, aby se kdokoliv mohl přihlásit a nebo zaregistrovat
                 *
                 *  authorizeRequests - získáme configurátor pro nastavení našich endpointů
                 *
                 *  antMatchers - nastavujeme naše veřejné endpointy
                 *  - permitAll - všichni se na tento endpoint dostanou, takže i anonymous.
                 *  - anonymous - dokud není uživatel autentikovaný, tak je anonymous. Takže se sem nedostane uživatel, který se už úspěšně autentikoval.
                 *
                 *  anyRequest.authenticated - nastavíme všechny ostatní endpoiny jako neveřejné
                 */
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/users").anonymous()
                .anyRequest().authenticated();

    }

    /**
     * Pro šifrování hesel. Je to nejpoužívanější algoritmus. Můžeme ho nazvat standardem.
     *
     * https://en.wikipedia.org/wiki/Bcrypt
     *
     * Anotace @Bean nad metodou říká, že metoda produkuje beanu, která má být manažovaná Spring containerem. Je to tkz. method-level annotation.
     * Během Java configuration (@Configuration) se metoda provede a vrátí se její hodnota pro registraci beany v BeanFactory.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  V UserResource potřebujeme přístup k AuthenticationManageru. Ten je defaultně nedostupný.
     *  Proto si ho přidáme do application contextu pomocí anotace @Bean, abychom si ho v UserResource mohli autowirnout.
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
