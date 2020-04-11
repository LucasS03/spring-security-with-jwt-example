package com.mydelivery.dto.in.profile;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthDTOIn {
	
	@NonNull
	private String username;
	@NonNull
	private String password;

}
