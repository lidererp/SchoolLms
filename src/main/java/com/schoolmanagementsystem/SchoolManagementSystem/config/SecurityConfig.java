package com.schoolmanagementsystem.SchoolManagementSystem.config;

import com.schoolmanagementsystem.SchoolManagementSystem.security.JwtAuthenticationEntryPoint;
import com.schoolmanagementsystem.SchoolManagementSystem.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final JwtAuthenticationEntryPoint point;
    private final JwtAuthenticationFilter filter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginRateLimitFilter loginRateLimitFilter;


    public SecurityConfig(JwtAuthenticationEntryPoint point,
                          JwtAuthenticationFilter filter,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          LoginRateLimitFilter loginRateLimitFilter) {
        this.point = point;
        this.filter = filter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginRateLimitFilter = loginRateLimitFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

                .cors(cors -> cors.disable()) // optional

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws", "/auth/login", "/user/forgot", "/user/reset-password",
                                "/contact", "/auth/refresh", "/auth/request-logout-all", "/auth/confirm-logout-all",
                                "/user/create", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/v2/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception.authenticationEntryPoint(point))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(loginRateLimitFilter, UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;

    }

}
