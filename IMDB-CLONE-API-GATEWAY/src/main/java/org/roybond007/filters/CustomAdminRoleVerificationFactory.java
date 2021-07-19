package org.roybond007.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomAdminRoleVerificationFactory implements GatewayFilterFactory<CustomAdminRoleVerificationFactory.Config>{

	public static class Config {

	}

	@Override
	public Class<Config> getConfigClass() {
		
		return Config.class;
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		
		return new CustomAdminRoleVerificationFilter();
	}

	
	
}
