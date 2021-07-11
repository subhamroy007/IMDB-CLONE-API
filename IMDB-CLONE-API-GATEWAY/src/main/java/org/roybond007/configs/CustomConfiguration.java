package org.roybond007.configs;

import java.time.Duration;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CustomConfiguration {

//	@Bean
//	public CorsWebFilter getCorsWebFilter() {
//		
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500", "http://192.168.43.235:5500"));
//		configuration.setAllowedHeaders(List.of("*"));
//		configuration.setAllowedMethods(List.of("*"));
//		configuration.setExposedHeaders(List.of("*"));
//		configuration.setAllowCredentials(true);
//		configuration.setMaxAge(Duration.ofMinutes(5));
//		
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", configuration);
//		
//		return new CorsWebFilter(source);
//	}
	
}
