package com.zholtikov.cloud_dove.config;

import com.zholtikov.cloud_dove.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(antMatcher("/swagger-ui/**"),
                                    antMatcher("/swagger-ui.html"),
                                    antMatcher("/v3/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/users/registration")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/users/**")).hasAnyRole(UserRole.MODERATOR.name(), UserRole.USER.name())
                            .requestMatchers(antMatcher(HttpMethod.GET, "/users/**")).hasAnyRole(UserRole.MODERATOR.name(), UserRole.USER.name())
                            .requestMatchers(antMatcher(HttpMethod.POST, "/api/uploads/**")).hasAnyRole(UserRole.MODERATOR.name(), UserRole.USER.name())
                            .requestMatchers(antMatcher(HttpMethod.PATCH, "/moderators/**")).hasRole(UserRole.MODERATOR.name())
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
