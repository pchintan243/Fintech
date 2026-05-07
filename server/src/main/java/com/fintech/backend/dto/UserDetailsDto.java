package com.fintech.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class UserDetailsDto extends User{
	/**
	 * User model object
	 */
	private com.fintech.backend.entity.User user;
	/**
	 * Constructor
	 * @param username User name
	 * @param password Password
	 * @param authorities Authorities
	 */
	public UserDetailsDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	/**
	 * Constructor
	 * @param user User object to get credentials
	 * @param authorities Authorities
	 */
	public UserDetailsDto(com.fintech.backend.entity.User user, Collection<? extends GrantedAuthority> authorities){
		this(user.getEmail(), user.getPassword(), authorities);
		this.user = user;
	}
}
