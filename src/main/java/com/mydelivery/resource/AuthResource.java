package com.mydelivery.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mydelivery.domain.Profile;
import com.mydelivery.domain.enums.PermissionEnum;
import com.mydelivery.dto.in.profile.AuthDTOIn;
import com.mydelivery.dto.in.profile.RegisterDTOIn;
import com.mydelivery.dto.out.profile.AuthDTOOut;
import com.mydelivery.dto.out.profile.NewProfileDTOOut;
import com.mydelivery.repository.ProfileRepository;
import com.mydelivery.security.jwt.JwtUtils;
import com.mydelivery.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthResource {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	ProfileRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthDTOIn request) {
		
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String accessToken = jwtUtils.generateJwtToken(authentication, jwtUtils.getAccessTokenMs());
		int accessTokenMs = jwtUtils.getAccessTokenMs();
		String refreshToken = jwtUtils.generateJwtToken(authentication, jwtUtils.getRefreshTokenMs());
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
										.map(item -> item.getAuthority())
										.collect(Collectors.toList());
		return ResponseEntity.ok(
			new AuthDTOOut(
				accessToken,
				refreshToken,
				accessTokenMs/1000,
				userDetails.getId(), 
				userDetails.getName(), 
				userDetails.getUsername(), 
				roles
			)
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody RegisterDTOIn request) {
		if (userRepository.existsByUsername(request.getUsername()))
			return ResponseEntity.status(409).body("MAIL_ALREADY_REGISTERED");
		
		if (userRepository.existsByCpf(request.getCpf()))
			return ResponseEntity.status(409).body("CPF_ALREADY_REGISTERED");
		
		Profile profile = new Profile(
							request.getName(),
							request.getCpf(),
							request.getUsername(),
							encoder.encode(request.getPassword()));

		Profile saved = userRepository.save(profile);
		if(saved.getId() == null)
			return ResponseEntity.status(500).body(
				"Houve um erro ao criar o usuário. Por favor tente novamente mais tarde."
			);

		return ResponseEntity.status(201).body(NewProfileDTOOut.fromProfile(saved));
	}
	
	@PostMapping("/token")
	public ResponseEntity<?> getAccessToken(@RequestHeader String refreshToken) {
		String newAccessToken = jwtUtils.newToken(refreshToken, jwtUtils.getAccessTokenMs());
		String newRefreshToken = jwtUtils.newToken(refreshToken, jwtUtils.getRefreshTokenMs());
		
		String r = jwtUtils.getBodyFromJwtToken(refreshToken).get("roles").toString();
		List<String> roles = new ArrayList<String>();
		if(r.contains(PermissionEnum.ROLE_CLIENT.toString())) roles.add(PermissionEnum.ROLE_CLIENT.toString());
		if(r.contains(PermissionEnum.ROLE_ADMIN.toString())) roles.add(PermissionEnum.ROLE_ADMIN.toString());
		if(r.contains(PermissionEnum.ROLE_DELIVERYMAN.toString())) roles.add(PermissionEnum.ROLE_DELIVERYMAN.toString());
		
		return ResponseEntity.ok(
			new AuthDTOOut(
				newAccessToken,
				newRefreshToken,
				jwtUtils.getAccessTokenMs()/1000,
				jwtUtils.getBodyFromJwtToken(refreshToken).get("id").toString(),
				jwtUtils.getBodyFromJwtToken(refreshToken).get("name").toString(),
				jwtUtils.getBodyFromJwtToken(refreshToken).getSubject().toString(),
				roles
			)
		);
	}
	
}
