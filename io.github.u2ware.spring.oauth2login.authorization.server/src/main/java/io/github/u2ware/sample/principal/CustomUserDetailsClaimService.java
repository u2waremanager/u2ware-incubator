package io.github.u2ware.sample.principal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.github.u2ware.sample.UserDetailsClaimService;


@Component
public class CustomUserDetailsClaimService implements UserDetailsClaimService{

	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public Map<String, Object> convertUserDetails(UserDetails userDetails) {
		Map<String, Object> response = new HashMap<>();
		
		
		
		CustomUserDetails d = (CustomUserDetails)userDetails;
		response.put("description", d.getDescription());
		logger.info(response);
		logger.info(response);
		logger.info(response);
		logger.info(response);
		logger.info(response);
		
		return response;
	}

	

}
