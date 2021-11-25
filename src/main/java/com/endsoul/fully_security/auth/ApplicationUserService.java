package com.endsoul.fully_security.auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ApplicationUserService implements UserDetailsService {
  @Qualifier("fake")
  private final ApplicationUserDao applicationUserDao;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return applicationUserDao
        .selectApplicationUserByUsername(username)
        .getOrElseThrow(
            () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
  }
}
