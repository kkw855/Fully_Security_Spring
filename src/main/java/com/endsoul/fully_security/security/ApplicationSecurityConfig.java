package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserRole.ADMIN;
import static com.endsoul.fully_security.security.ApplicationUserRole.ADMINTRAINEE;
import static com.endsoul.fully_security.security.ApplicationUserRole.STUDENT;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // antMatcher can override previous matcher
    http.csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/**")
        .permitAll()
        .antMatchers("/api/**")
        .hasRole(STUDENT.name())
        //        .antMatchers(HttpMethod.DELETE, "/management/api/**")
        //        .hasAuthority(STUDENT_WRITE.getPermission())
        //        .antMatchers(HttpMethod.POST, "/management/api/**")
        //        .hasAuthority(STUDENT_WRITE.getPermission())
        //        .antMatchers(HttpMethod.PUT, "/management/api/**")
        //        .hasAuthority(STUDENT_WRITE.getPermission())
        //        .antMatchers(HttpMethod.GET, "/management/api/**")
        //        .hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
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
