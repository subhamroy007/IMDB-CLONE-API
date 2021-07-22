package org.roybond007.configs;

import org.roybond007.filters.CustomPrincipalLoaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter{

	
	private final CustomPrincipalLoaderFilter customPrincipalLoaderFilter;
	
	@Autowired
	public CustomSecurityConfiguration(CustomPrincipalLoaderFilter customPrincipalLoaderFilter) {
		this.customPrincipalLoaderFilter = customPrincipalLoaderFilter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.cors()
			.disable()
			.addFilterBefore(customPrincipalLoaderFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers("/movie/fetch/**", "/posters/**", "/trailers/**").permitAll()
				.anyRequest().authenticated()
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				
	}
	
	

}
