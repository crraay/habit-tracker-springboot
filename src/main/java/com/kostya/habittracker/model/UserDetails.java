package com.kostya.habittracker.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

import com.kostya.habittracker.entity.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private final User user;

	public UserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); // Customize as needed
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
