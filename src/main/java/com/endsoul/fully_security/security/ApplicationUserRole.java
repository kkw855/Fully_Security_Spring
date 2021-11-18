package com.endsoul.fully_security.security;

import com.google.common.collect.Sets;
import java.util.Set;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationUserRole {
  STUDENT(Sets.newHashSet()),
  ADMIN(Sets.newHashSet());

  private final Set<ApplicationUserPermission> permissions;
}
