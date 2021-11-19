package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserPermission.*;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationUserRole {
  STUDENT(HashSet.empty()),
  ADMIN(HashSet.of(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

  private final Set<ApplicationUserPermission> permissions;
}
