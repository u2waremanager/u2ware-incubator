package io.github.u2ware.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTokenAuthenticationProvider implements AuthenticationProvider {

	protected Log logger = LogFactory.getLog(getClass());
	
	private PasswordEncoder passwordEncoder;
	
	public UserTokenAuthenticationProvider(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String username = authentication.getName();
        Object password = authentication.getCredentials();

        logger.info("authenticate 1: "+username);
        logger.info("authenticate 2: "+password);

    	return null;
	}
}