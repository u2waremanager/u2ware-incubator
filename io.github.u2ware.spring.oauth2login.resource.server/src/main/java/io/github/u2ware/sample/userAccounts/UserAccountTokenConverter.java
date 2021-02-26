package io.github.u2ware.sample.userAccounts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import io.github.u2ware.sample.core.UserAccount;


@Component
public class UserAccountTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {//extends JwtAuthenticationConverter{


	private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter ;
	
	private @Autowired UserAccountRepository userAccountRepository;
	
	public UserAccountTokenConverter() {
		jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	}
	
	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		String name = jwt.getSubject();
		UserAccount userAccount = userAccountRepository.findOneByPrincipalName(name);
		Collection<GrantedAuthority> authorities = extractAuthorities(jwt, userAccount);
		return new UserAccountToken(jwt, authorities, name, userAccount);
	}
	
	protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt, UserAccount userAccount) {

		Set<String> authorities = new HashSet<>();
		this.jwtGrantedAuthoritiesConverter.convert(jwt).forEach(a->{authorities.add(a.getAuthority());});
		if(userAccount != null) {
			userAccount.getAuthorities().forEach(a->{authorities.add(a.getAuthority());});
		}
		
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority));
		}
		return grantedAuthorities;
	}
}
