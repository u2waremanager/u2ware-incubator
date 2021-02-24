package io.github.u2ware.sample.secured;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

	private Log log = LogFactory.getLog(getClass());
	
	@GetMapping("/principal")
	public Principal principal(Principal principal) {
		log.info(principal);
		log.info(principal.getClass());
		log.info(principal.getName());
		return principal;//System.currentTimeMillis();
	}
	
	@GetMapping("/authentication")
	public Authentication authentication(Authentication authentication) {
		log.info(authentication);
		log.info(authentication.getClass());
		log.info(authentication.getCredentials());
		log.info(authentication.getDetails());
		log.info(authentication.getPrincipal());
		log.info(authentication.getAuthorities());
		return authentication;
	}
	
	@GetMapping("/oauth2User")
	public OAuth2User oauth2User(@AuthenticationPrincipal OAuth2User oauth2User) {
		log.info(oauth2User);
		log.info(oauth2User.getClass());
		log.info(oauth2User.getName());
		return oauth2User;
	}

	@GetMapping("/authorizedClient")
	public OAuth2AuthorizedClient authorizedClient(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		log.info(authorizedClient);
		log.info(authorizedClient.getClass());
		return authorizedClient;//System.currentTimeMillis();
	}	
	
}
