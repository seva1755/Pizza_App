package com.example.pizza.configuration;

import com.example.pizza.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebSecurityConfig {
    @Autowired
    UserService userService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        AuthenticationManager manager = builder.build();
        return http
                .cors().disable()
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/cafe/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/cafe/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/cafe/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/pizza/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/pizza/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/pizza/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/pizza/**").permitAll()
                .antMatchers(HttpMethod.GET, "/cafe/full/**").permitAll()
                .antMatchers("/cafes/**").permitAll()
                .antMatchers("/pizzas/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .authenticationManager(manager)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
