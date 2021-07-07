package org.roybond007.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.roybond007.model.entity.UserEntity;
import org.roybond007.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserEntityRepository userEntityRepository;
	
	@Autowired
	public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
		this.userEntityRepository = userEntityRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		Optional<UserEntity> result = userEntityRepository.findByUserId(userId);
		
		if(result.isPresent()) {
			UserEntity target = result.get();
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			String[] roles = target.getRoles();
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
			UserDetails userDetails = User
										.withUsername(userId)
										.password(target.getPassword())
										.accountExpired(!target.isActive())
										.accountLocked(!target.isActive())
										.credentialsExpired(!target.isActive())
										.disabled(!target.isActive())
										.authorities(authorities)
										.build();
			return userDetails;
 		}
		
		throw new UsernameNotFoundException(userId + " not found");
	}

}
