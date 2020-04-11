package com.mydelivery.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mydelivery.domain.Profile;
import com.mydelivery.repository.ProfileRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	ProfileRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile user = repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

		return UserDetailsImpl.build(user);
	}

}
