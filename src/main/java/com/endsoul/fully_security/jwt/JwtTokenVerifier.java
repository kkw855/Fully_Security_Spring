package com.endsoul.fully_security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenVerifier extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {

    final String authorizationHeader = httpServletRequest.getHeader("Authorization");

    if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    try {
      final String SECRET_KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecure";
      final String token = authorizationHeader.replace("Bearer ", "");

      final Jws<Claims> claimsJws = Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(
              SECRET_KEY.getBytes()))
          .build()
          .parseClaimsJws(token);

      final Claims body = claimsJws.getBody();

      final String username = body.getSubject();

      final List<Map<String, String>> authorities = List.ofAll((List<Map<String, String>>) body.get(
          "authorities"));

      final List<Seq<String>> map = authorities.map(m -> m.values());
      map.fold

      Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.map(auth -> new SimpleGrantedAuthority(auth.get("role")))

      Authentication authentication = new UsernamePasswordAuthenticationToken(
          username,
          null,
          authorities
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }
}
