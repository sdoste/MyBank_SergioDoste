package com.midterm.MyBank.config;

import com.midterm.MyBank.service.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,  "/accounts/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.POST, "/accounts/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.PUT, "/accounts/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH, "/accounts/**").hasAnyRole("ADMIN", "ACCOUNTHOLDER", "THIRDPARTY")
                .antMatchers("/*").authenticated()
                .anyRequest().permitAll();


        return http.build();
    }
}
