package com.mydelivery.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mydelivery.domain.enums.PermissionEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class Profile implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String name;
	private String cpf; 
	private String username;
	private String password;
	private String photo;
	private List<Address> address = new ArrayList<Address>();
	@JsonIgnore
	private Set<PermissionEnum> permissions;
	
	@Getter
	@Setter
	@ToString
	public class Address {
		private String state;
		private String city;
		private String block;
		private String street;
		private String number;
		private String reference;
	}

	public Profile(String name, String cpf, String username, String password) {
		this.setName(name);
		this.setCpf(cpf);
		this.setUsername(username);
		this.setPassword(password);
		this.setPermissions(Set.of(PermissionEnum.ROLE_CLIENT));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.toString()))
				.collect(Collectors.toList());
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
	
	public boolean isClient() {
		return permissions.contains(PermissionEnum.ROLE_CLIENT);
	}
	
	public boolean isAdmin() {
		return permissions.contains(PermissionEnum.ROLE_ADMIN);
	}
	
	public boolean isDeliveryMan() {
		return permissions.contains(PermissionEnum.ROLE_DELIVERYMAN);
	}
}
