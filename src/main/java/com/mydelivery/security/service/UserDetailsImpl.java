package com.mydelivery.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mydelivery.domain.Profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String username;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl(String id, String name, String username, String password, 
							Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(Profile profile) {
		List<GrantedAuthority> authorities = profile.getPermissions().stream()
				.map(role -> new SimpleGrantedAuthority(role.toString()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				profile.getId(), 
				profile.getName(), 
				profile.getUsername(),
				profile.getPassword(), 
				authorities);
	}

	@Override
	public String getUsername() {
		return username;
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
