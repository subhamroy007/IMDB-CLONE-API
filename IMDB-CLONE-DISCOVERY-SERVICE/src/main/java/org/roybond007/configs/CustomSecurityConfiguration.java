package org.roybond007.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration  extends WebSecurityConfigurerAdapter {
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().ignoringAntMatchers("/eureka/**")
			.and()
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.httpBasic();
	
		
	}
	
}
