package org.roybond007.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(TOKEN_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        //Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) userDetails.getAuthorities();
        
        //System.out.println(authorities);
        
        claims.put("password", userDetails.getPassword());
        //claims.put("roles", authorities);
        claims.put("active", userDetails.isEnabled());
        
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD))
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        
        Claims claims = extractAllClaims(token);
        
        final String password = claims.get("password", String.class);
        final boolean isActive = claims.get("active", Boolean.class);
        //final Collection<Object> authorities = claims.get("roles", Collection.class);
        
       
        
        return (username.equals(userDetails.getUsername()) 
        		&& !isTokenExpired(token) 
        		&& password.equals(userDetails.getPassword())
        		&& isActive
        		);
    }
}
