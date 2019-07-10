package io.github.u2ware.sample;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class UserInfoEndpoint extends BearerTokenExtractor{

	public static final String PATH = "/user/info";
        
	protected Log logger = LogFactory.getLog(getClass());

	private TokenStore tokenStore;
	
	public UserInfoEndpoint(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@GetMapping("/")
	public @ResponseBody Object userInfo( Principal principal) {
        logger.info("-----------------------------------");
        logger.info("principal: "+principal);
        logger.info("-----------------------------------");
		return principal;
	}
    
    
    @GetMapping(value=PATH)
    public @ResponseBody ResponseEntity<Object> info(HttpServletRequest request) {
    	
    	try {
            logger.info("info: "+tokenStore.getClass());
            
        	String token = super.extractHeaderToken(request);
            OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
            //OAuth2Authentication authentication = this.tokenStore.readAuthentication(accessToken);
            
    		Map<String, Object> attributes = new HashMap<>();
    		if (accessToken == null || accessToken.isExpired()) {
    			attributes.put("active", false);
    			return ResponseEntity.ok(attributes);
            }


    		attributes.put("active", true);
    		attributes.put("exp", accessToken.getExpiration().getTime());
    		attributes.put("expDate", new DateTime(accessToken.getExpiration().getTime()).toString());
            attributes.put("scope", accessToken.getScope().stream().collect(Collectors.joining(" ")));
    		attributes.putAll(accessToken.getAdditionalInformation());
            
            logger.info("info: "+attributes);

			return ResponseEntity.ok(attributes);
    	}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
	}
}
