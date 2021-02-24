package io.github.u2ware.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{

	private static final String DEFAULT_AUTHORITY_PREFIX = "SCOPE_";

	private static final Collection<String> WELL_KNOWN_AUTHORITIES_CLAIM_NAMES =
			Arrays.asList("scope", "scp");

	private String authorityPrefix = DEFAULT_AUTHORITY_PREFIX;

	private String authoritiesClaimName;

	/**
	 * Extract {@link GrantedAuthority}s from the given {@link Jwt}.
	 *
	 * @param jwt The {@link Jwt} token
	 * @return The {@link GrantedAuthority authorities} read from the token scopes
	 */
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : getAuthorities(jwt)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(this.authorityPrefix + authority));
		}
		grantedAuthorities.add(new SimpleGrantedAuthority("Hello"));
		grantedAuthorities.add(new SimpleGrantedAuthority("World"));
		
		return grantedAuthorities;
	}

	/**
	 * Sets the prefix to use for {@link GrantedAuthority authorities} mapped by this converter.
	 * Defaults to {@link JwtGrantedAuthoritiesConverter#DEFAULT_AUTHORITY_PREFIX}.
	 *
	 * @param authorityPrefix The authority prefix
	 * @since 5.2
	 */
	public void setAuthorityPrefix(String authorityPrefix) {
		Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
		this.authorityPrefix = authorityPrefix;
	}

	/**
	 * Sets the name of token claim to use for mapping {@link GrantedAuthority authorities} by this converter.
	 * Defaults to {@link JwtGrantedAuthoritiesConverter#WELL_KNOWN_AUTHORITIES_CLAIM_NAMES}.
	 *
	 * @param authoritiesClaimName The token claim name to map authorities
	 * @since 5.2
	 */
	public void setAuthoritiesClaimName(String authoritiesClaimName) {
		Assert.hasText(authoritiesClaimName, "authoritiesClaimName cannot be empty");
		this.authoritiesClaimName = authoritiesClaimName;
	}

	private String getAuthoritiesClaimName(Jwt jwt) {

		if (this.authoritiesClaimName != null) {
			return this.authoritiesClaimName;
		}

		for (String claimName : WELL_KNOWN_AUTHORITIES_CLAIM_NAMES) {
			if (jwt.containsClaim(claimName)) {
				return claimName;
			}
		}
		return null;
	}

	private Collection<String> getAuthorities(Jwt jwt) {
		String claimName = getAuthoritiesClaimName(jwt);

		if (claimName == null) {
			return Collections.emptyList();
		}

		Object authorities = jwt.getClaim(claimName);
		if (authorities instanceof String) {
			if (StringUtils.hasText((String) authorities)) {
				return Arrays.asList(((String) authorities).split(" "));
			} else {
				return Collections.emptyList();
			}
		} else if (authorities instanceof Collection) {
			return (Collection<String>) authorities;
		}

		return Collections.emptyList();
	}


//	@Override
//	public AbstractAuthenticationToken convert(Jwt jwt) {
//		
//		Collection<GrantedAuthority> authorities = new ArrayList<>();
//		
//		for (String authority : getAuthorities(jwt)) {
//			authorities.add(new SimpleGrantedAuthority(this.authorityPrefix + authority));
//		}
//		
//	  return new CustomAuthenticationToken(jwt, authorities, "aaaaaaaaa");
//	}
//	
//	public static class CustomAuthenticationToken extends JwtAuthenticationToken{
//
//		public CustomAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
//			super(jwt, authorities, name);
//		}
//
//	}
//	
//	public void setJwtGrantedAuthoritiesConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
//		Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
//		this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
//	}

}
