package com.lab2.Spring.Security.Core.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class SecurityConfig {
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // Allow encoded slashes and newline characters
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedPercent(true); // Allows encoded %
        return firewall;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/menu").permitAll()
                                .requestMatchers("/manager/**").hasRole("MANAGER")
                                .requestMatchers("/client/**").hasRole("CLIENT")
                                .requestMatchers("/actuator/**").hasRole("MANAGER")
                                .requestMatchers("/actuator/metrics/**").hasRole("MANAGER")
                                .anyRequest().authenticated()
                )
                .httpBasic() // Enable Basic Authentication
                .and()
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("manager")
                        .password("{noop}password")
                        .roles("MANAGER")
                        .build(),
                User.withUsername("client")
                        .password("{noop}password")
                        .roles("CLIENT")
                        .build()
        );
    }
}
