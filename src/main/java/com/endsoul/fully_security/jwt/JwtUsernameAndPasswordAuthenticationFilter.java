package com.endsoul.fully_security.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Try;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    final Try<BufferedReader> readerTry = Try.of(request::getReader);

    final Authentication authentication =
        readerTry
            .map(
                reader ->
                    new Gson().fromJson(reader, UsernameAndPasswordAuthenticationRequest.class))
            .map(
                authenticationRequest ->
                    new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(), authenticationRequest.getPassword()))
            .getOrElseThrow(
                e ->
                    new InternalAuthenticationServiceException(
                        "cannot parse username and password in request", e));

    return authenticationManager.authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {

    final String token =
        Jwts.builder()
            .setSubject(authResult.getName())
            .claim("authorities", authResult.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(
                java.sql.Date.valueOf(
                    LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
            .signWith(secretKey)
            .compact();

    response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
  }
}
