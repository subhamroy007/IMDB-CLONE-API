package org.roybond007.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomPrincipalLoaderFilter extends OncePerRequestFilter {

	@Value("${gateway.key}")
	private String gatewayKey;

	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

		if(request.getRequestURI().contains("/trailers") || request.getRequestURI().contains("/posters")) {
			return true;
		}
		
		return false;
	}
	
	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		
		
		if(!verifiyGatewayOrgin(request)) {
			
			System.err.println("token not verified");
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setContentLength(0);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return ;
		}
		
		
		String userId = request.getHeader("userId");
		String password = request.getHeader("password");
		boolean isActive = Boolean.parseBoolean(request.getHeader("active"));
		String role = request.getHeader("roles");
		String roles[] = null;
		if(role != null)
			roles = role.split(" ");
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if(roles != null)
			for (String r: roles) {
				authorities.add(new SimpleGrantedAuthority(r));
			}
		
		if((userId != null) && (password != null) && isActive && (roles != null) && (roles.length != 0)) {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(userId, password, authorities);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}
		
		filterChain.doFilter(request, response);
		
		SecurityContextHolder.clearContext();
	}


	private boolean verifiyGatewayOrgin(HttpServletRequest request) {
		
		String target = request.getHeader("gateway-key");
		
		if(target != null && target.equals(gatewayKey))
			return true;
		
		return false;
	}
	
}
