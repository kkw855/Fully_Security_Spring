package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserPermission.*;
import static com.endsoul.fully_security.security.ApplicationUserRole.*;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/**")
        .permitAll()
        .antMatchers("/api/**")
        .hasRole(STUDENT.name())
        .antMatchers(HttpMethod.DELETE, "/management/api/**")
        .hasAuthority(STUDENT_WRITE.getPermission())
        .antMatchers(HttpMethod.POST, "/management/api/**")
        .hasAuthority(STUDENT_WRITE.getPermission())
        .antMatchers(HttpMethod.PUT, "/management/api/**")
        .hasAuthority(STUDENT_WRITE.getPermission())
        .antMatchers(HttpMethod.GET, "/management/api/**")
        .hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
            .authorities(STUDENT.getGrantedAuthorities().toJavaSet())
            .build();

    final UserDetails lindaUser =
        User.builder()
            .username("linda")
            .password(passwordEncoder.encode("password123"))
            .authorities(ADMIN.getGrantedAuthorities().toJavaSet())
            .build();

    final UserDetails tomUser =
        User.builder()
            .username("tom")
            .password(passwordEncoder.encode("password123"))
            .authorities(ADMINTRAINEE.getGrantedAuthorities().toJavaSet())
            .build();

    return new InMemoryUserDetailsManager(annaSmithUser, lindaUser, tomUser);
  }
}
