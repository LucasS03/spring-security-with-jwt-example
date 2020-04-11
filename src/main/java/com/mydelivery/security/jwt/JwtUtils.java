package com.mydelivery.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mydelivery.security.service.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;

@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${mydelivery.app.jwtSecret}")
	private String jwtSecret;

	@Value("${mydelivery.app.accessTokenExpirationMs}")
	@Getter private int accessTokenMs;
	
	@Value("${mydelivery.app.refreshTokenExpirationMs}")
	@Getter private int refreshTokenMs;

	public String generateJwtToken(Authentication authentication, int ms) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.claim("id", userPrincipal.getId())
				.claim("name", userPrincipal.getName())
				.claim("roles", userPrincipal.getAuthorities()
												.stream()
												.map(item -> item.getAuthority())
												.collect(Collectors.toList()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + ms))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
	
	public String newToken(String token, int ms) {
		validateJwtToken(token);
		Claims c = getBodyFromJwtToken(token);
		
		return Jwts.builder()
				.setSubject(c.getSubject())
				.claim("id", c.get("id"))
				.claim("name", c.get("name"))
				.claim("roles", c.get("roles"))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + ms))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Claims getBodyFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
