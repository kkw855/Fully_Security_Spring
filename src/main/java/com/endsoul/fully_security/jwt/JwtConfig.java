package com.endsoul.fully_security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
  private String secretKey;
  private String tokenPrefix;
  private Integer tokenExpirationAfterDays;

  public String getAuthorizationHeader() {
    return HttpHeaders.AUTHORIZATION;
  }
}
