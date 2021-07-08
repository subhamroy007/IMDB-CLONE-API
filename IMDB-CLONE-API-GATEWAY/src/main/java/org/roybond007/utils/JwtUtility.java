package org.roybond007.utils;

import java.util.Date;
import java.util.function.Function;

import org.roybond007.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtility {

	@Value("${token.secret_key}")
    private String TOKEN_SECRET_KEY;

	@Value("${token.validity_period}")
	private long TOKEN_VALIDITY_PERIOD;
	
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserEntity userDetails) {
        final String username = extractUsername(token);
        
        Claims claims = extractAllClaims(token);
        
        final String password = claims.get("password", String.class);
        final boolean isActive = claims.get("active", Boolean.class);
        //final Collection<Object> authorities = claims.get("roles", Collection.class);
        
       
        
        return (username.equals(userDetails.getUserId()) 
        		&& !isTokenExpired(token) 
        		&& password.equals(userDetails.getPassword())
        		&& isActive
        		);
    }
}

