package com.mydelivery.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mydelivery.domain.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String>{
	
	Optional<Profile> findByUsername(String username);
	
	Optional<Profile> findByCpf(String cpf);

	Boolean existsByUsername(String username);

	Boolean existsByCpf(String cpf);
	
}
