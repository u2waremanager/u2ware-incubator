package io.github.u2ware.sample.secured;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;


@Component
public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter{

	private static final String DEFAULT_AUTHORITY_PREFIX = "ROLE_";
	
	public CustomJwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix(DEFAULT_AUTHORITY_PREFIX);
        setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
	}

	@Override
	protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		
		Collection<GrantedAuthority> grantedAuthorities =  super.extractAuthorities(jwt);
		grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_AUTHORITY_PREFIX+"Hello22ccssssc"));
		grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_AUTHORITY_PREFIX+"World22ccsssscc2"));
		
		return grantedAuthorities;
	}
}
