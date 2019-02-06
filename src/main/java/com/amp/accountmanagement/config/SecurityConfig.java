package com.amp.accountmanagement.config;

import com.amp.accountmanagement.controller.interceptor.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@Order(HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationFilter authenticationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .disable()
        .csrf()
        .disable()
        .httpBasic()
        .disable();

    http.sessionManagement()
        .sessionCreationPolicy(STATELESS);
    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
