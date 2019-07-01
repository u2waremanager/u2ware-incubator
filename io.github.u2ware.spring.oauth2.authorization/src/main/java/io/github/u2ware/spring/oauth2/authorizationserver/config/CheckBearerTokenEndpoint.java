package io.github.u2ware.spring.oauth2.authorizationserver.config;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class CheckBearerTokenEndpoint extends BearerTokenExtractor{

	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private CheckTokenEndpoint checkTokenEndpoint;
	
	private TokenStore tokenStore;
	
	public CheckBearerTokenEndpoint(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	
    @GetMapping(value="/oauth/check_token_wrapper")
    public @ResponseBody Map<String, ?> check_token(HttpServletRequest request) {
    	String token = super.extractHeaderToken(request);
    	logger.info("token: "+token);
    	return checkTokenEndpoint.checkToken(token);
    }
	
	
	
    @GetMapping(value="/oauth/info")
    public @ResponseBody Map<String, ?> info(HttpServletRequest request) {
    	String token = super.extractHeaderToken(request);
    	
    	logger.info("info: "+tokenStore); //JwtTokenStore
    	logger.info("info: "+tokenStore.getClass());
    	
		OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
		Map<String, Object> attributes = new HashMap<>();
		if (accessToken == null || accessToken.isExpired()) {
			attributes.put("active", false);
			return attributes;
		}

		OAuth2Authentication authentication = this.tokenStore.readAuthentication(token);

		attributes.put("active", true);
		attributes.put("exp", accessToken.getExpiration().getTime());
		attributes.put("scope", accessToken.getScope().stream().collect(Collectors.joining(" ")));
		attributes.put("sub", authentication.getName());
		
		attributes.put("id", authentication.getName());
		attributes.put("expDate", new DateTime(accessToken.getExpiration().getTime()).toString());

		return attributes;
	}
}
