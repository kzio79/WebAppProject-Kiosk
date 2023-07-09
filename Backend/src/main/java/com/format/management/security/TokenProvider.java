package com.format.management.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.format.management.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenProvider {
	
	//시크릿 키
	private static final String SECRET_KEY = 
			"FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXX"
			+ "WUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPiDF0rMaT";
	
	public String create(UserEntity userEntity) {
		//토큰 기간
		Date expiryDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));
		
		return Jwts.builder()
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.setSubject(userEntity.getId())
				.setIssuer("format")
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.compact();
	}
	
	public String validateAndGetUserId(String token) {
			Claims claims = Jwts.parser()
					.setSigningKey(SECRET_KEY)
					.parseClaimsJws(token)
					.getBody();
			
			return claims.getSubject();
	}
	
}
