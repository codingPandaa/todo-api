package com.swapnil.todo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	public String generateToken(Authentication authentication) {

		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

		// creating token
		String token = Jwts.builder().subject(username).issuedAt(currentDate).expiration(expireDate).signWith(key())
				.compact();

		return token;
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	// get username from token
	public String getUsername(String token) {

		Claims claims = (Claims) Jwts.parser().setSigningKey(key()).build().parse(token).getPayload();
		String username = claims.getSubject();
		return username;
	}

	// validate jwt token
	public boolean validateToken(String token) {
		Jwts.parser().setSigningKey(key()).build().parse(token);
		return true;
	}

}
