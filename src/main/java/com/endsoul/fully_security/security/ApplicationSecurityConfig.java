package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserRole.STUDENT;

import com.endsoul.fully_security.auth.ApplicationUserService;
import com.endsoul.fully_security.jwt.JwtConfig;
import com.endsoul.fully_security.jwt.JwtTokenVerifier;
import com.endsoul.fully_security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final ApplicationUserService applicationUserService;
  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // antMatcher can override previous matcher
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(
            new JwtUsernameAndPasswordAuthenticationFilter(
                authenticationManager(), jwtConfig, secretKey))
        .addFilterAfter(
            new JwtTokenVerifier(jwtConfig, secretKey),
            JwtUsernameAndPasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/**")
        .permitAll()
        .antMatchers("/api/**")
        .hasRole(STUDENT.name())
        .anyRequest()
        .authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  public DaoAuthenticationProvider daoAuthenticationProvider() {
    final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(applicationUserService);
    return provider;
  }
}
