package com.endsoul.fully_security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import java.io.IOException;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;
  private final SecretKey secretKey;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {

    final String authorizationHeader =
        httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());

    if (Strings.isNullOrEmpty(authorizationHeader)
        || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    final String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

    try {
      final Jws<Claims> claimsJws =
          Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

      final Claims body = claimsJws.getBody();

      final String username = body.getSubject();

      final java.util.List<java.util.Map<String, String>> listMap =
          (java.util.List<java.util.Map<String, String>>) body.get("authorities");

      final List<Map<String, String>> authorities = List.ofAll(listMap);

      final List<SimpleGrantedAuthority> simpleGrantedAuthorities =
          authorities
              .map(map -> HashMap.ofAll(map).getOrElse("role", ""))
              .filter(authority -> !authority.isEmpty())
              .map(SimpleGrantedAuthority::new);

      Authentication authentication =
          new UsernamePasswordAuthenticationToken(
              username, null, simpleGrantedAuthorities.toJavaList());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
