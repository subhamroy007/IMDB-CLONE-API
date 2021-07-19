package org.roybond007.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class CustomAdminRoleVerificationFilter implements GatewayFilter{

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		if(!hasAdminRole(exchange)) {
			System.err.println("does not have admin role");
			return errorMsg(exchange);
		}
		
		return chain.filter(exchange);
	}

	private boolean hasAdminRole(ServerWebExchange exchange) {
		String roles = exchange.getRequest().getHeaders().get("roles").get(0);
	
		
		if(roles != null && roles.contains("ADMIN"))
			return true;
		
		return false;
	}

	private Mono<Void> errorMsg(ServerWebExchange exchange) {
		
		ServerHttpResponse response = exchange.getResponse();
		
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		
		
		return response.setComplete();
	}
	
}
