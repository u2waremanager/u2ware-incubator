package com.u2ware.sample.oauth2.resource;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import static org.springframework.security.oauth2.provider.token.AccessTokenConverter.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAccessTokenHelper implements InitializingBean{

	protected Log logger = LogFactory.getLog(getClass());
	
	@Value("${com.u2ware.sample.oauth2.resource.JwtAccessTokenHelper.privateKey}")
	private String jwtPrivateKey;

	@Value("${com.u2ware.sample.oauth2.resource.JwtAccessTokenHelper.publicKey}")
	private String jwtPublicKey;

	@Autowired
	private ObjectMapper objectMapper;
	
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(jwtPrivateKey);
		jwtAccessTokenConverter.setVerifierKey(jwtPublicKey);
		logger.info(jwtAccessTokenConverter);
		logger.info(jwtAccessTokenConverter.getClass());
		logger.info(jwtAccessTokenConverter.getKey());
		logger.info(jwtAccessTokenConverter.getAccessTokenConverter().getClass());
	}
	
	public String encode(Map<String, Object> accessTokenInformation) throws Exception {
		
		accessTokenInformation.remove("headers");
		if(accessTokenInformation.containsKey(EXP)) {
			Object value = accessTokenInformation.get(EXP);
			if(value instanceof Number) {
				Long longValue = ((Number)value).longValue();
				accessTokenInformation.put(EXP, longValue);
			}
		}

		
		OAuth2AccessToken token = jwtAccessTokenConverter.extractAccessToken("", accessTokenInformation); //DefaultAccessTokenConverter
		logger.info("token_______________");
		logger.info(token.getClass());// DefaultOAuth2AccessToken
		logger.info(token);
		logger.info(token.getValue());
		logger.info(token.getScope());
		logger.info(token.getExpiration());
		logger.info(token.getTokenType());
		

		OAuth2Authentication auth = jwtAccessTokenConverter.extractAuthentication(accessTokenInformation);
		logger.info("authentication_____________________");
		logger.info(auth.getClass());	//OAuth2Authentication
		logger.info(auth.getName());
		logger.info(auth.getAuthorities());
		logger.info(auth.getOAuth2Request());
		logger.info(auth.getOAuth2Request().getClass());
		
		OAuth2AccessToken accessToken = jwtAccessTokenConverter.enhance(token, auth);
		logger.info("accessToken_____________________");
		logger.info(accessToken);
		logger.info(accessToken.getValue());
		logger.info(accessToken.getValue());
		logger.info(accessToken.getScope());
		logger.info(accessToken.getExpiration());
		logger.info(accessToken.getTokenType());
		return accessToken.getValue();
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> decode(String token) throws Exception {
		Jwt jwt = JwtHelper.decode(token);
		Map<String,String> headers = JwtHelper.headers(token);
		Map<String,Object> claims = objectMapper.readValue(jwt.getClaims(), Map.class);
		claims.put("headers", headers);
		return claims;
	}
}
