package com.mydelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mydelivery.domain.Profile;
import com.mydelivery.dto.in.profile.NewProfileDTOIn;
import com.mydelivery.repository.ProfileRepository;

@Service
public class ProfileService {
	
	@Autowired
	private ProfileRepository repository;
	private PasswordEncoder encoder;
	
	public final Profile save(NewProfileDTOIn newProfileDTO) {
		Profile profile = newProfileDTO.toProfile(encoder);
		
		return save(profile);
	}
	
	public Profile save(Profile profile){
//		Profile p = repository.findByMail(profile.getMail());
//		if(p != null) return null;

		return repository.save(profile);
	}
	
}
