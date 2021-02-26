package io.github.u2ware.sample.userAccounts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import io.github.u2ware.sample.core.UserAccount;

@Transient
@SuppressWarnings("serial")
public class UserAccountToken extends JwtAuthenticationToken{

	public static final String USER_ACCOUNT = "userAccount";

	private UserAccount userAccount;
	
	public UserAccountToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name, UserAccount userAccount) {
		super(jwt, authorities, name);
		this.userAccount = userAccount;
	}
	
	@Override
	public Map<String, Object> getTokenAttributes() {
		Map<String, Object> response = new HashMap<>();
		response.putAll(super.getTokenAttributes());
		response.put(USER_ACCOUNT, userAccount);
		return response;
	}

}
