package com.endsoul.fully_security.auth;

import io.vavr.control.Option;

public interface ApplicationUserDao {
  Option<ApplicationUser> selectApplicationUserByUsername(final String username);
}
