package com.endsoul.fully_security.security;

import static com.endsoul.fully_security.security.ApplicationUserPermission.COURSE_READ;
import static com.endsoul.fully_security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.endsoul.fully_security.security.ApplicationUserPermission.STUDENT_READ;
import static com.endsoul.fully_security.security.ApplicationUserPermission.STUDENT_WRITE;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@Getter
public enum ApplicationUserRole {
  STUDENT(HashSet.empty()),
  ADMIN(HashSet.of(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
  ADMINTRAINEE(HashSet.of(COURSE_READ, STUDENT_READ));

  private final Set<ApplicationUserPermission> permissions;

  public Set<? extends GrantedAuthority> getGrantedAuthorities() {
    return getPermissions()
        .map(p -> new SimpleGrantedAuthority(p.getPermission()))
        .add(new SimpleGrantedAuthority("ROLE_" + this.name()));
  }
}
