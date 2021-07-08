package org.roybond007.filters;

import java.util.ArrayList;
import java.util.Set;

import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.JwtUtility;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public class CustomTokenValidationFilter implements GatewayFilter{

	
	private final  JwtUtility jwtUtility;
	
	
	private final UserEntityRepository userEntityRepository;
	
	
	
	public CustomTokenValidationFilter(JwtUtility jwtUtility, UserEntityRepository userEntityRepository) {
		super();
		this.jwtUtility = jwtUtility;
		this.userEntityRepository = userEntityRepository;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		if(!isHeaderAvailable(exchange)) {
			return errorMsg(exchange);
		}
		
		String token = exchange.getRequest().getHeaders().getOrEmpty("Authorization").get(0);
		String userId = exchange.getRequest().getHeaders().getOrEmpty("u_id").get(0);
		
		System.out.println(token);
		System.out.println(userId);
		
		Mono<Void> result = userEntityRepository.findByUserId(userId).flatMap(entity -> {
			
			if(entity == null) {
				System.out.println("entity not found");
				return errorMsg(exchange);
			}
			
			if(!jwtUtility.validateToken(token, entity)) {
				System.out.println("token not valid");
				return errorMsg(exchange);
			}
			
			System.out.println("adding header");
			addHeaders(exchange, token, entity.getRoles());
			
			return chain.filter(exchange);
		});
		
		return result;
	}

	private void addHeaders(ServerWebExchange exchange, String token, String[]  roles) {
		
		Claims claims = jwtUtility.extractAllClaims(token);
		
		String userId = claims.getSubject();
		String password = claims.get("password", String.class);
		boolean active = claims.get("active", Boolean.class);
		String roleHeader = "";
		
		for (String role : roles) {
			roleHeader = roleHeader + role;
		}
		
		roleHeader.trim();
		
		exchange
			.getRequest()
			.mutate()
			.header("userId", userId)
			.header("password", password)
			.header("active", String.valueOf(active))
			.header("roles", roles)
			.build();
		
	}

	private Mono<Void> errorMsg(ServerWebExchange exchange) {
		
		ServerHttpResponse response = exchange.getResponse();
		
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		
		
		return response.setComplete();
	}

	private boolean isHeaderAvailable(ServerWebExchange exchange) {
		
		return exchange.getRequest().getHeaders().containsKey("Authorization");
	}

	
	
}
