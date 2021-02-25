package io.github.u2ware.sample.authorization;

import static io.github.u2ware.sample.ApplicationOauth2AuthorizationServerConfig.JWT_JWKS_JSON;
import static io.github.u2ware.sample.ApplicationOauth2AuthorizationServerConfig.JWT_USER_INFO;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * authorization 권한 / 인가  /오쏘로제이션/
 * 
 * @author u2ware
 */
@Controller
public class Oauth2AuthorizationEndpoint {

    private @Autowired OAuth2AuthorizationService oauth2AuthorizationService;

	@GetMapping(JWT_JWKS_JSON)
	public @ResponseBody Object jwks() {
		return oauth2AuthorizationService.jwks();
	}
	
	
	@GetMapping(JWT_USER_INFO)
	public @ResponseBody ResponseEntity<?> info(HttpServletRequest request) throws Exception{
        String idToken = extractHeaderToken(request);
        try{
    		return ResponseEntity.ok(oauth2AuthorizationService.decode(idToken));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
	}
	

	
	protected String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders("Authorization");
		while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith("Bearer".toLowerCase()))) {
				String authHeaderValue = value.substring("Bearer".length()).trim();
                return authHeaderValue;
			}
		}
		return null;
	}
	
	
	
}
