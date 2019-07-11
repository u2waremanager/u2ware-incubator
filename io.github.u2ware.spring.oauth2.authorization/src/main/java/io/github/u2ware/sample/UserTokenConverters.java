package io.github.u2ware.sample;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class UserTokenConverters extends DefaultUserAuthenticationConverter {
	
    protected Log logger = LogFactory.getLog(getClass());

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {

        logger.info("convertUserAuthentication1: ");
        logger.info("convertUserAuthentication1: "+authentication.getPrincipal());
        
        UserToken token = (UserToken)authentication.getPrincipal();

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("id", authentication.getName());

        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        response.putAll(token.getInfo());
        
		return response;
	}

}
