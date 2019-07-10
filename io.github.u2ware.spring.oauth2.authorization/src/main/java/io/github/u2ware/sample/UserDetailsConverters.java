package io.github.u2ware.sample;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class UserDetailsConverters extends DefaultUserAuthenticationConverter {
	
    protected Log logger = LogFactory.getLog(getClass());

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        logger.info("convertUserAuthentication1: "+authentication);
        logger.info("convertUserAuthentication2: "+authentication.getClass());
        logger.info("convertUserAuthentication3: "+authentication.getPrincipal()); //->UserDetailsService

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("id", authentication.getName());

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }

		return response;
	}

}
