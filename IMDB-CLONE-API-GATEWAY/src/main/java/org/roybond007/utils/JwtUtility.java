package org.roybond007.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
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
        if(claims != null)
        	return claimsResolver.apply(claims);
        return null;
    }
    public Claims extractAllClaims(String token) {
        
    	Claims claims = null;
    	
    	try {
    		claims = Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token).getBody();
		} catch (RuntimeException ex) {
			System.err.println(ex.getLocalizedMessage());
		}
    	
    	return claims;
    }

    public Boolean isTokenExpired(String token) {
        try {
        	return extractExpiration(token).before(new Date());
		} catch (Exception e) {
			return true;
		}
    }


    public Boolean validateToken(String token, UserEntity userDetails) {
        final String username = extractUsername(token);
        
        Claims claims = extractAllClaims(token);
        
        if(claims == null)
        	return false;
        
        final String password = claims.get("password", String.class);
        final boolean isActive = claims.get("active", Boolean.class);
        
       
        
        return (username.equals(userDetails.getUserId()) 
        		&& !isTokenExpired(token) 
        		&& password.equals(userDetails.getPassword())
        		&& isActive
        		);
    }
}

