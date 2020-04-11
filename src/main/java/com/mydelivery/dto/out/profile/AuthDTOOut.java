package com.mydelivery.dto.out.profile;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthDTOOut {
	
	private String accessToken;
	private String refreshToken;
	private String type = "Bearer";
	private int expireIn;
	private String id;
	private String name;
	private String username;
	private List<String> roles;

	public AuthDTOOut(String accessToken, String refreshToken, int expireIn, String id, String name, String username, List<String> roles) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expireIn = expireIn;
		this.id = id;
		this.name = name;
		this.username = username;
		this.roles = roles;
	}
	
}
