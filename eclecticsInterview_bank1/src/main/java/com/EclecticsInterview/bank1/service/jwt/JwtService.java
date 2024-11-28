package com.EclecticsInterview.bank1.service.jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private static final String SECRET = "491CC9413AD65136AB3C8DA05DE0FCA53D7A9DD48F4AEBF235C1EF3D20B2A03258D16F3BCB783578F8A70CDFF73DE918D75190D8C4233BF5F1B3F3716B8EF14D";
	private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);
	
	public String generateToken(UserDetails userDetails)
	{
		Map<String, String> claims = new HashMap<>();
        claims.put("iss", "https://secure.home.com");
        claims.put("name", "eclectics");
		String outputToken = Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(VALIDITY)) )
				.signWith(generateKey())
				.compact()
		;
		return outputToken;
	}
	
	private SecretKey generateKey()
	{
		byte [] decodedKey = Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	public String extractUsername(String jwt) {
		Claims claims = getClaims(jwt);
        return claims.getSubject();
	}
	
	private Claims getClaims(String jwt) {
        return Jwts.parser()
                 .verifyWith(generateKey())
                 .build()
                 .parseSignedClaims(jwt)
                 .getPayload();
    }

	public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
	}
	
}
