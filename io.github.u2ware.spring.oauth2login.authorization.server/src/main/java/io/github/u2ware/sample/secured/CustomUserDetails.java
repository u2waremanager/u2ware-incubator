package io.github.u2ware.sample.secured;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
public @Data @Builder @AllArgsConstructor @NoArgsConstructor class CustomUserDetails implements UserDetails{

	
	private Collection<? extends GrantedAuthority> authorities;
	public String password;
	public String username;
	public boolean accountNonExpired = true;
	public boolean accountNonLocked = true;
	public boolean credentialsNonExpired = true;
	public boolean enabled = true;

	
	
	public String description;
}
