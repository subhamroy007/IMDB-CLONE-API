package org.roybond007.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomPrincipalLoaderFilter extends OncePerRequestFilter {

	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String userId = request.getHeader("userId");
		String password = request.getHeader("password");
		boolean isActive = Boolean.parseBoolean(request.getHeader("active"));
		String role = request.getHeader("roles");
		String []roles = null;
		if(role != null)
			roles = role.split(" ");
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if(roles != null)
			for (String r: roles) {
				authorities.add(new SimpleGrantedAuthority(r));
			}
		
		if(userId != null && password != null && isActive && roles != null && roles.length != 0) {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(userId, password, authorities);
			authenticationToken.setAuthenticated(true);
			SecurityContextHolder.createEmptyContext().setAuthentication(authenticationToken);
		}
		
		filterChain.doFilter(request, response);
		
		SecurityContextHolder.clearContext();
	}
	
}
