package io.github.u2ware.sample;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class AuthorizationServerEndpoint {

	protected Log logger = LogFactory.getLog(getClass());
	
	@GetMapping("/")
	public @ResponseBody Object logon( Principal principal) {
        logger.info("logon: "+principal);
		return principal;
	}

}
