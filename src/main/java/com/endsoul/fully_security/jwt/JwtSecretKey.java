package com.endsoul.fully_security.jwt;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class JwtSecretKey {
  private final JwtConfig jwtConfig;

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
  }
}
