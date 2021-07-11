package org.roybond007.configs;

import org.roybond007.filters.CustomPrincipalLoaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration extends WebSecurityConfigurerAdapter{

	private final UserDetailsService userDetailsService;
	
	private final CustomPrincipalLoaderFilter customPrincipalLoaderFilter;
	
	@Autowired
	public CustomSecurityConfiguration(UserDetailsService userDetailsService
			, CustomPrincipalLoaderFilter customPrincipalLoaderFilter) {
		this.userDetailsService = userDetailsService;
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
				.antMatchers(HttpMethod.POST, "/user/account/signup", "/user/account/signin").permitAll()
				.anyRequest().authenticated()
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(getPasswordEnCoder());
	}

	@Bean
	public PasswordEncoder getPasswordEnCoder() {
		
		return new BCryptPasswordEncoder(10);
	}
	
}
