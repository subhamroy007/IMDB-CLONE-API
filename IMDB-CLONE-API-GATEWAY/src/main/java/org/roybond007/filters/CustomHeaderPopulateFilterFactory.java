package org.roybond007.filters;

import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomHeaderPopulateFilterFactory implements GatewayFilterFactory<CustomHeaderPopulateFilterFactory .Config>{

	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	private UserEntityRepository userEntityRepository;
	
	public static class Config {
		
	}

	@Override
	public Class<Config> getConfigClass() {
		
		return Config.class;
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		
		return new CustomHeaderPopulateFilter(jwtUtility, userEntityRepository);
	}

}
