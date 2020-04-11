package com.mydelivery.dto.in.profile;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.mydelivery.domain.Profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProfileDTOIn {

	private String name;
	private String cpf; 
	private String mail;
	private String password;
	
	public Profile toProfile(final PasswordEncoder encoder) {
		return new Profile(name, cpf, mail, encoder.encode(password));
	}
	
//	private Profile createUser(final PasswordEncoder encoder) {
//		return Profile.newProfile();
//					.setName(name);
//					.setCpf(cpf)
//					.setMail(mail)
//					.setPassword(encoder.encode(password))
//					.setPermissions(Set.of(PermissionEnum.ROLE_CLIENT));
//	}
	
}
