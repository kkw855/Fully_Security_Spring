package com.endsoul.fully_security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UsernameAndPasswordAuthenticationRequest {
  private final String username;
  private final String password;
}
