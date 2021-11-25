package com.endsoul.fully_security.auth;

import static com.endsoul.fully_security.security.ApplicationUserRole.ADMIN;
import static com.endsoul.fully_security.security.ApplicationUserRole.ADMINTRAINEE;
import static com.endsoul.fully_security.security.ApplicationUserRole.STUDENT;

import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {
  private PasswordEncoder passwordEncoder;

  @Override
  public Option<ApplicationUser> selectApplicationUserByUsername(final String username) {
    return getApplicationUsers().find(user -> user.getUsername().equals(username));
  }

  private List<ApplicationUser> getApplicationUsers() {
    return List.of(
        new ApplicationUser(
            "annasmith",
            passwordEncoder.encode("password"),
            STUDENT.getGrantedAuthorities(),
            true,
            true,
            true,
            true),
        new ApplicationUser(
            "linda",
            passwordEncoder.encode("password123"),
            ADMIN.getGrantedAuthorities(),
            true,
            true,
            true,
            true),
        new ApplicationUser(
            "tom",
            passwordEncoder.encode("password123"),
            ADMINTRAINEE.getGrantedAuthorities(),
            true,
            true,
            true,
            true));
  }
}
