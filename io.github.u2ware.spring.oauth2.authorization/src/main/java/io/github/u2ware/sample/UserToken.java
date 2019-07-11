package io.github.u2ware.sample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class UserToken implements UserDetails{

	private String password;
	private String username;
	private Set<GrantedAuthority> authorities = new HashSet<>();
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Map<String, Object> info = new HashMap<>();
	
	public UserToken(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public UserToken(String username, String password, Map<String, Object> info) {
		this.username = username;
		this.password = password;
		this.info = info;
	}
	
	
	public void addRoles(String... roles) {
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
	}
	public boolean hasRole(String role) {
		return true;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
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


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Map<String, Object> getInfo() {
		return info;
	}


	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}
}	