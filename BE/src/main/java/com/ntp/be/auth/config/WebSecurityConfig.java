package com.ntp.be.auth.config;

import com.ntp.be.auth.exceptions.RESTAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    private static final String[] PUBLIC_APIS = {
            "/api/ntpshop/auth/**",          // register, login, verify
            "/oauth2/success",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        // ✅ public API (auth, oauth2)
                        .requestMatchers(PUBLIC_APIS).permitAll()

                        // ✅ User API: USER hoặc ADMIN
                        .requestMatchers("/api/ntpshop/**").hasAnyRole("USER", "ADMIN")

                        // ✅ User API: ADMIN
                        .requestMatchers("/api/ntpshop/admin/**").hasRole("ADMIN")

                        // ✅ còn lại thì phải login
                        .anyRequest().authenticated()
                )
                // OAuth2 login config
                .oauth2Login((oauth2login) ->
                        oauth2login.defaultSuccessUrl("/oauth2/success")
                                .loginPage("/oauth2/authorization/google")
                )
                // Xử lý exception
                .exceptionHandling((exception) ->
                        exception.authenticationEntryPoint(new RESTAuthenticationEntryPoint())
                )
                // Thêm JWT filter
                .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}