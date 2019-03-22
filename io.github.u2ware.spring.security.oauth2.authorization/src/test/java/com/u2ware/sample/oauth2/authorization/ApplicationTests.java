package com.u2ware.sample.oauth2.authorization;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	protected MockMvc mockMvc;

	protected MockMvcWrapper $;

	@Before
	public void before() {
		$ = new MockMvcWrapper(mockMvc,"");
	}
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private OAuth2ClientProperties oauth2ClientProperties;

	@Autowired
	private JwtAccessTokenHelper jwtAccessTokenHelper;
	
	
//	@Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.enabled}")
//	protected Boolean jwtEnabled;
//	
//	@Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.publicKey}")
//	protected String jwtPublicKey;
	
	@Test
	public void contextLoads() throws Exception {
		
		String username = securityProperties.getUser().getName(); 
		String password = securityProperties.getUser().getPassword();
		String clientId = oauth2ClientProperties.getClientId();
		String clientSecret = oauth2ClientProperties.getClientSecret();
		
		logger.info(username);
		logger.info(password);
		logger.info(clientId);
		logger.info(clientSecret);
		
		RequestPostProcessor w = httpBasic(clientId, clientSecret);

		
	    ////////////////////////////////////////
		//
		////////////////////////////////////////
	    $.GET("/oauth/token_key").is2xx("key");
	    Assert.assertEquals(jwtAccessTokenHelper.getVerifierKey(), $.content("key.value"));
		
	    // http://localhost:8081/oauth/authorize?grant_type=authorization_code&scope=read&client_id=trusted&response_type=code&redirect_uri=http://naver.com
	    // http://localhost:8081/oauth/authorize?grant_type=authorization_code&scope=read&client_id=trusted&response_type=token&redirect_uri=http://naver.com
	    
	    //////////////////////////////////////
	    // Authorization Code Flow
	    //////////////////////////////////////
	    MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
	    params1.add("grant_type", "authorization_code");
	    params1.add("scope", "read");
	    params1.add("client_id", clientId);
	    params1.add("redirect_uri", "http://naver.com");
	    params1.add("response_type", "code");
	    
	    $.GET("/oauth/authorize").P(params1).is3xx();

	    
	    //////////////////////////////////////
	    // Implicit Grant Flow
	    //////////////////////////////////////
	    MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
	    params2.add("grant_type", "implicit");
	    params2.add("scope", "read");
	    params2.add("redirect_url", "redirect_url");
	    
	    //ImplicitResourceDetails d;
	    $.GET("/oauth/authorize").P(params2).is3xx();
	    //AuthorizationEndpoint f;
	    
	    
	    
	    //////////////////////////////////////
	    // Client Credentials Flow
	    //////////////////////////////////////
	    MultiValueMap<String, String> params5 = new LinkedMultiValueMap<>();
	    params5.add("grant_type", "client_credentials");
	    params5.add("scope", "read"); 

	    $.POST("/oauth/token").W(w).P(params5).is2xx("token5");
	    String accessToken5 = $.content("token5.access_token");
	    logger.info(accessToken5);
	    logger.info(jwtAccessTokenHelper.decode(accessToken5));
	    String refreshToken5 = $.content("token5.refresh_token");
	    Assert.assertNull(refreshToken5);
	    

	    //////////////////////////////////////
	    // Resource Owner Password Credentials Flow
	    // ResourceOwnerPasswordResourceDetails
	    //////////////////////////////////////
	    MultiValueMap<String, String> params3 = new LinkedMultiValueMap<>();
	    params3.add("grant_type", "password");
	    params3.add("username", username);
	    params3.add("password", password);
	    params3.add("scope", "read"); 
	    
	    $.POST("/oauth/token").W(w).P(params3).is2xx("token3");
	    String accessToken3 = $.content("token3.access_token");
	    Map<String,Object> accessTokenInfo3 = jwtAccessTokenHelper.decode(accessToken3);
	    logger.info(accessToken3);
	    logger.info(accessTokenInfo3);
	    
	    
	    Long exp = ((Number)accessTokenInfo3.get("exp")).longValue() * 1000L;
	    logger.info(exp);
	    logger.info(new Date(exp));
	    String refreshToken3 = $.content("token3.refresh_token");
	    Assert.assertNotNull(refreshToken3);
	    
//	    //////////////////////////////////////
//	    // Refresh Token
//	    //////////////////////////////////////
//	    MultiValueMap<String, String> params4 = new LinkedMultiValueMap<>();
//	    params4.add("grant_type", "refresh_token");
//	    params4.add("refresh_token", refreshToken3);
//	    params4.add("scope", "read"); 
//
//	    //boot-2.1.2
////	    $.POST("/oauth/token").W(w).P(params4).is5xx("token4");
//	    
//	    //boot-1.5.X
//	    String accessToken4 = $.content("token4.access_token");
//	    String refreshToken4 = $.content("token4.refresh_token");
//
//	    Assert.assertNotEquals(accessToken3, accessToken4);
//	    Assert.assertNotEquals(refreshToken3, refreshToken4);
//	    logger.info(accessToken3);
//	    logger.info(accessToken4);
//	    logger.info(refreshToken3);
//	    logger.info(refreshToken4);
//	    
//
//	    //$.scan(applicationContext);
//	    $.scan(applicationContext, TokenEndpoint.class);
	    
//      .pathMapping("/oauth/authorize", authorizePath);
//      .pathMapping("/oauth/token", tokenPath)
//      .pathMapping("/oauth/confirm_access", confirmPath)
//      .pathMapping("/oauth/check_token", checkTokenPath)
//      .pathMapping("/oauth/token_key", tokenKeyPath)
	}	
}