package io.github.u2ware.sample;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

public class UserInfoConverter extends DefaultUserAuthenticationConverter {
	
    protected Log logger = LogFactory.getLog(getClass());

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("sub", authentication.getName());
		response.put("name", authentication.getName());
		response.put("id", authentication.getName());
		response.put("aaa", "gggg");
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }

        logger.info("convertUserAuthentication: "+response);
		return response;
	}

}
