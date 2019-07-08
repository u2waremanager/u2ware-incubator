package com.example.demo.signin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author u2waremanager@gmail.com
 */
@SuppressWarnings("serial")
public class Principal implements UserDetails {

	private String username;
	private @JsonIgnore String password;
	private @JsonIgnore boolean enabled = true;
	private @JsonIgnore boolean accountNonExpired = true;
	private @JsonIgnore boolean accountNonLocked = true;
	private @JsonIgnore boolean credentialsNonExpired = true;
	private @JsonIgnore Collection<GrantedAuthority> authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = new ArrayList<GrantedAuthority>(authorities);
	}

	/////////////////////////////////////////
	//
	/////////////////////////////////////////
	public void setRoles(GrantedAuthority... authorities) {
		this.setAuthorities(Arrays.asList(authorities));
	}

	public void setRoles(String... roles) {
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		this.setAuthorities(authorities);
	}

	public String[] getRoles() {
		if (authorities == null)
			return new String[] {};
		String[] roles = new String[authorities.size()];
		int i = 0;
		for (GrantedAuthority authority : authorities) {
			roles[i] = authority.getAuthority();
			i++;
		}
		return roles;
	}

	public boolean hasRoles(String... roles) {
		if (authorities == null)
			return false;
		for (GrantedAuthority authority : authorities) {
			for (String role : roles) {
				if (authority.getAuthority().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}
}