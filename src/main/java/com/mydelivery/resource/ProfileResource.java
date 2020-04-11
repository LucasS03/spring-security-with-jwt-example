package com.mydelivery.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mydelivery.dto.in.profile.NewProfileDTOIn;
import com.mydelivery.dto.out.profile.NewProfileDTOOut;
import com.mydelivery.service.ProfileService;

@RestController
@RequestMapping(value="/profiles")
public class ProfileResource {
		
	@Autowired
	private ProfileService service;
	
	/*
	 * POST
	 * 
	 * */
	@PostMapping
	public ResponseEntity<NewProfileDTOOut> createUser(@RequestBody NewProfileDTOIn profile) {
		NewProfileDTOOut out = NewProfileDTOOut.fromProfile(service.save(profile));
		return ResponseEntity.ok().body(out);
	}
}
