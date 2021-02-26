package io.github.u2ware.sample.userAccounts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserAccountTokenBuilder {

	public static UserAccountTokenBuilder username(String name){
		return new UserAccountTokenBuilder(name);
	}
	
	private String name;
	private String[] roles = new String[] {};
	
	private UserAccountTokenBuilder(String name) {
		this.name = name;
	}
	
	public UserAccountTokenBuilder roles(String... roles) {
		this.roles = roles;
		return this;
	}
	
	public UserAccountToken build() {
		String id = UUID.randomUUID().toString();
		Jwt jwt = Jwt.withTokenValue(id).jti(id).header("name", name).subject(name).build();

		List<GrantedAuthority> authorities = new ArrayList<>();
		for(String role : roles){
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return new UserAccountToken(jwt, authorities, name, null);
	}
	
}
