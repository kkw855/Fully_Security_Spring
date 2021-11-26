package com.endsoul.fully_security.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Try;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
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

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    final Try<BufferedReader> readerTry = Try.of(request::getReader);

    final UsernameAndPasswordAuthenticationRequest authenticationRequest =
        readerTry
            .map(
                reader ->
                    new Gson().fromJson(reader, UsernameAndPasswordAuthenticationRequest.class))
            .getOrElseThrow(
                e ->
                    new InternalAuthenticationServiceException(
                        "cannot parse username and password in request", e));

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(), authenticationRequest.getPassword());

    return authenticationManager.authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    final String SECRET_KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecure";

    final String token =
        Jwts.builder()
            .setSubject(authResult.getName())
            .claim("authorities", authResult.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
            .signWith(
                Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes()))
            .compact();

    response.addHeader("Authorization", "Bearer " + token);
  }
}
