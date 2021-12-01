package com.endsoul.fully_security.jwt;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JsonAuthorities {
  private List<JsonRole> authorities;
}
