package io.github.u2ware.sample;

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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class BearerTokenEndpoint extends BearerTokenExtractor{

        
	protected Log logger = LogFactory.getLog(getClass());

	private TokenStore tokenStore;
	
	public BearerTokenEndpoint(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	
    @GetMapping(value="/oauth/user")
    public @ResponseBody ResponseEntity<Object> info(HttpServletRequest request) {
    	
    	try {
        	String token = super.extractHeaderToken(request);
        	
        	logger.info("info: "+tokenStore); //JwtTokenStore
        	logger.info("info: "+tokenStore.getClass());
        	
    		OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
    		Map<String, Object> attributes = new HashMap<>();
    		if (accessToken == null || accessToken.isExpired()) {
    			attributes.put("active", false);
    			return ResponseEntity.ok(attributes);
    		}

    		OAuth2Authentication authentication = this.tokenStore.readAuthentication(token);

    		attributes.put("active", true);
    		attributes.put("exp", accessToken.getExpiration().getTime());
    		attributes.put("scope", accessToken.getScope().stream().collect(Collectors.joining(" ")));
    		attributes.put("sub", authentication.getName());
    		
    		attributes.put("id", authentication.getName());
    		attributes.put("expDate", new DateTime(accessToken.getExpiration().getTime()).toString());

			return ResponseEntity.ok(attributes);
    	}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
	}
}
