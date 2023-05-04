package ss.martin.platform.spring.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ss.martin.platform.service.SystemUserService;
import ss.martin.platform.spring.security.AuthUsernamePasswordFilter;
import ss.martin.platform.spring.security.JwtRequestFilter;

/**
 * Spring security configuration.
 * @author Alexandr Omeluaniuk
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity/*(debug = true)*/
public class SecurityConfig {
    /** Authentication entry point. */
    @Autowired
    private AuthenticationEntryPoint authEntryPoint;
    /** Authentication manager. */
    @Autowired
    private AuthenticationManager authManager;
    /** Authentication success handler. */
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    /** Authentication failure handler. */
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    /** Logout success handler. */
    @Autowired
    private LogoutSuccessHandler logoutSuccesshandler;
    /** System user service. */
    @Autowired
    private SystemUserService systemUserService;
    /** Platform configuration. */
    @Autowired
    private PlatformConfiguration configuration;
    /** JWT request filter. */
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests().requestMatchers(
                        configuration.getNavigation().getProtectedRest() + "/**",
                        "/api/platform/security/**", 
                        "/api/platform/entity/**"
                ).authenticated().and()
                .authorizeHttpRequests().requestMatchers("/**").permitAll().and()
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginPage(configuration.getNavigation().getLoginPage()).permitAll().and()
                .logout().deleteCookies("JSESSIONID").logoutUrl(configuration.getNavigation().getLogout())
                .logoutSuccessHandler(logoutSuccesshandler)
                .invalidateHttpSession(true)
                .and().exceptionHandling().authenticationEntryPoint(authEntryPoint);
        // whitelist
        if (configuration.getJwt() != null) {
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
        // CSP
        if (configuration.getContentSecurityPolicy() != null) {
            http.headers().xssProtection().and().contentSecurityPolicy(configuration.getContentSecurityPolicy());
        }
        systemUserService.superUserCheck();
        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall httpFirewall) {
        return (web) -> {
            web.httpFirewall(httpFirewall);
//            web.ignoring().requestMatchers(
//                    "/assets/**", 
//                    "/locales/*", 
//                    "/*.json", 
//                    "/*.js", 
//                    "/*.txt", 
//                    "/*.ico", 
//                    "/*.html", 
//                    "/WEB-INF/jsp/*"
//            );
        };
    }
    
    /**
     * Spring security HTTP firewall.
     * @return 
     */
    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowSemicolon(true);
        return firewall;
    }
    /**
     * Create custom authentication filter.
     * @return authentication filter.
     */
    private Filter authFilter() {
        AuthUsernamePasswordFilter filter = new AuthUsernamePasswordFilter();
        filter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(configuration.getNavigation().getLogin(), "POST")
        );
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }
}
