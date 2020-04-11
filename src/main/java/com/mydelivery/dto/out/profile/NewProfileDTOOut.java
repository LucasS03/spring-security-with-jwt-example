package com.mydelivery.dto.out.profile;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.mydelivery.domain.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewProfileDTOOut implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String mail;
	private String cpf;
	private List<String> permissions;
	
	public NewProfileDTOOut(Profile p) {
		this.setId(p.getId());
		this.setName(p.getName());
		this.setMail(p.getUsername());
		this.setCpf(p.getCpf());
		this.setPermissions(p.getAuthorities()
							 .stream()
							 .map(i -> i.getAuthority())
							 .collect(Collectors.toList()));
	}
	
	public static NewProfileDTOOut fromProfile(Profile p) {
		return new NewProfileDTOOut(p);
	}
	
}
