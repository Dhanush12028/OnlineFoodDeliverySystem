package com.cts.OnlineFoodDeliverySystem.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customerUserDetailsService;

    @Lazy
    @Autowired
    @Qualifier("restaurantAdminDetailsService")
    private UserDetailsService restaurantAdminUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider customerAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setAuthoritiesMapper(user -> Collections.singletonList(() -> "CUSTOMER")); // Assign CUSTOMER role
        return authProvider;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(restaurantAdminUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setAuthoritiesMapper(user -> Collections.singletonList(() -> "ADMIN")); // Assign ADMIN role
        return authProvider;
    }

    @Bean
    public SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/customer/**", "/") // Apply this chain to /customer/** and potentially root
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/images/**", "/webjars/**", "/css/**", "/js/**", "/customer/register").permitAll()
                .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/customer/login")
                .loginProcessingUrl("/customer/login")
                .defaultSuccessUrl("/customer/dashboard")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/customer/login?error")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Consider a specific logout for customers if needed
                .logoutSuccessUrl("/")
                .permitAll()
                .deleteCookies("JSESSIONID")
            )
            .authenticationProvider(customerAuthProvider());
        return http.build();
    }

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**")
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/admin/register", "/images/**", "/webjars/**", "/css/**", "/js/**").permitAll() // Add any public admin resources
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/admin/login?error")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout")) // Optional: specific admin logout
                .logoutSuccessUrl("/admin/login?logout")
                .permitAll()
                .deleteCookies("JSESSIONID")
            )
            .authenticationProvider(adminAuthProvider());
        return http.build();
    }
}