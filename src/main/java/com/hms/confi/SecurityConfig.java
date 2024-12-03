package com.hms.confi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    )throws Exception
    {
        //h(cd)2
        http.csrf().disable().cors().disable();
       // http.addFilterBefore(jwtFilter, AuthenticationFilter.class);
       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        //haap
        http.authorizeHttpRequests().anyRequest().permitAll();

//        http.authorizeHttpRequests().
//                requestMatchers("/api/v1/users/login","/api/v1/users/signup","/api/v1/users/signup-property-owner")
//                .permitAll()
//                .requestMatchers("/api/v1/country/addCountry").hasAnyRole("OWNER","ADMIN")
//                .requestMatchers("/api/v1/review/").hasAnyRole("USER")
//                .anyRequest().authenticated();

        return http.build();
    }
}
