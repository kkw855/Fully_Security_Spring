package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserRole.*;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        // Basic64 ID-PASSWORD
        .httpBasic();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    final UserDetails annaSmithUser =
        User.builder()
            .username("annasmith")
            .password(passwordEncoder.encode("password"))
            .roles(STUDENT.name()) // ROLE_STUDENT
            .build();

    final UserDetails lindaUser =
        User.builder()
            .username("linda")
            .password(passwordEncoder.encode("password123"))
            .roles(ADMIN.name()) // ROLE_ADMIN
            .build();

    return new InMemoryUserDetailsManager(annaSmithUser, lindaUser);
  }
}
