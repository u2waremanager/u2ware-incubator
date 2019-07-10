package io.github.u2ware.sample;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@FrameworkEndpoint
public class UserInfoEndpoint extends BearerTokenExtractor {

	public static final String PATH = "/user/info";
        
	protected Log logger = LogFactory.getLog(getClass());

	private @Autowired TokenStore tokenStore;
	private @Autowired ObjectMapper objectMapper;
	
    @SuppressWarnings("unchecked")
	@GetMapping(value=PATH)
    public @ResponseBody ResponseEntity<Object> info(HttpServletRequest request) {
    	
    	try {
            logger.info("UserInfoEndpoint: "+tokenStore.getClass());
            
        	String token = super.extractHeaderToken(request);
            OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
            // OAuth2Authentication authentication = this.tokenStore.readAuthentication(accessToken);
            // logger.info("info2: "+accessToken);
            // logger.info("info3: "+accessToken.getAdditionalInformation());
            // logger.info("info4: "+authentication);
            // logger.info("info5: "+authentication.getClass());
            // logger.info("info6: "+authentication.getPrincipal());
    		// if (accessToken == null || accessToken.isExpired()) {
    		// 	attributes.put("active", false);
    		// 	return ResponseEntity.ok(attributes);
            // }
            
            Map<String, Object> attributes = objectMapper.convertValue(accessToken, Map.class);
            attributes.remove(OAuth2AccessToken.ACCESS_TOKEN);
            logger.info("UserInfoEndpoint: "+attributes);
			return ResponseEntity.ok(attributes);
			
    	}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
	}
}
