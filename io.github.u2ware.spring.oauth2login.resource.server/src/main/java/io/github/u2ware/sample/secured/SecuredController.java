package io.github.u2ware.sample.secured;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

	private Log log = LogFactory.getLog(getClass());
	
	@GetMapping("/user")
	public Map<String,Object> principal(
			Principal principal, 
			Authentication authentication) {
		
		//CommonOAuth2Provider.GOOGLE;
		
		log.info(principal);
		log.info(principal.getClass());
		log.info(principal.getName());
		log.info(authentication);
		log.info(authentication.getClass());
		log.info(authentication.getCredentials());
		log.info(authentication.getDetails());
		log.info(authentication.getPrincipal());
		log.info(authentication.getAuthorities());

		Map<String,Object> response = new HashMap<>();
		response.put("principal---", principal);
		response.put("authentication---", authentication);
		
		return response;
	}
	
}
