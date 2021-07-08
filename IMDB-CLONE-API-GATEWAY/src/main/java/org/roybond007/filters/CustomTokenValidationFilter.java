package org.roybond007.filters;

import org.roybond007.model.entity.UserEntity;
import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
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
		
		
		UserEntity userEntity = userEntityRepository.findByUserId(userId).block();
		
			
		
		if(userEntity == null) {
			return errorMsg(exchange);
		}
		
		if(!jwtUtility.validateToken(token, null)) {
			return errorMsg(exchange);
		}
		
		addHeaders(exchange, token);
		
		return chain.filter(exchange);
	}

	private void addHeaders(ServerWebExchange exchange, String token) {
		
		Claims claims = jwtUtility.extractAllClaims(token);
		
		exchange
			.getRequest()
			.mutate()
			.header("userId", claims.getSubject())
			.header("password", claims.get("password", String.class))
			.header("active", claims.get("active", String.class))
			.header("roles", claims.get("roles", String[].class))
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
