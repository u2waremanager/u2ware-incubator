package io.github.u2ware.sample;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class ApplicationOauth2LoginConfig extends WebSecurityConfigurerAdapter {
	
	
	public final String AUTHORIZATION_ENDPOINT_BASE_URI = "/oauth2/authorization"; //OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
	public final String REDIRECTION_ENDPOINT_BASE_URI = "/login/oauth2/code/*"; //OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI
	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http
		.authorizeRequests(authorize -> authorize
		    .anyRequest().authenticated()
		)
        .oauth2Login(oauth2->oauth2
			.authorizationEndpoint(authorization -> authorization
				.baseUri(AUTHORIZATION_ENDPOINT_BASE_URI) // or authorizationRequestResolver 
//				.authorizationRequestResolver(this.authorizationRequestResolver(clientRegistrationRepository))
//				.authorizationRequestRepository(this.authorizationRequestRepository())
			)
			.redirectionEndpoint(redirection -> redirection
				.baseUri(REDIRECTION_ENDPOINT_BASE_URI) // Default... ;
			)
//			.tokenEndpoint(token -> token
//				.accessTokenResponseClient(this.accessTokenResponseClient())
//			)
//                .userInfoEndpoint(userInfo -> userInfo
//	    			.customUserType(GitHubOAuth2User.class, "github")
//	    			.customUserType(GoogleOAuth2User.class, "google")
//                )        		
        		//.defaultSuccessUrl("/logon", true)
        );
    }

    
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver arr = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, AUTHORIZATION_ENDPOINT_BASE_URI);
        arr.setAuthorizationRequestCustomizer(customizer -> customizer.additionalParameters(params -> params.put("aaaaa", "aaaaaa")));
        return  arr;
    }
	
	
    //////////////////////////
    //
    ///////////////////////////////
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository(){

    	return new AuthorizationRequestRepository<OAuth2AuthorizationRequest>(){
			
			private Log logger = LogFactory.getLog(getClass());

			private HttpSessionOAuth2AuthorizationRequestRepository r = new HttpSessionOAuth2AuthorizationRequestRepository();
			
			@Override
			public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
				logger.info("loadAuthorizationRequest: "+request);
				return r.loadAuthorizationRequest(request);
			}

			@Override
			public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
				logger.info("saveAuthorizationRequest: "+authorizationRequest.getAdditionalParameters());
				logger.info("saveAuthorizationRequest: "+request.getRequestURI());
				r.saveAuthorizationRequest(authorizationRequest, request, response);
			}

			@Override
			public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
				logger.info("removeAuthorizationRequest: "+request);
				return r.removeAuthorizationRequest(request);
			}
		};
	}

    //////////////////////////
    //
    ///////////////////////////////
	private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = 
          new DefaultAuthorizationCodeTokenResponseClient(); 
        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter()); 

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = 
          new OAuth2AccessTokenResponseHttpMessageConverter(); 
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter()); 
        tokenResponseHttpMessageConverter.setTokenResponseParametersConverter(new CustomTokenResponseParametersConverter());
        
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
          new FormHttpMessageConverter(), tokenResponseHttpMessageConverter)); 
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler()); 
        
        accessTokenResponseClient.setRestOperations(restTemplate); 
        return accessTokenResponseClient;
    }
	
	private static class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

		
		private Log logger = LogFactory.getLog(getClass());
		
	    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;
	    
	    public CustomRequestEntityConverter() {
	        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
	    }
	    
	    @Override
	    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
	        RequestEntity<?> entity = defaultConverter.convert(req);
	        
	        MultiValueMap<String, String> params = (MultiValueMap<String,String>) entity.getBody();
	        params.add("test2", "extra2");
	        
	        OAuth2AuthorizationRequest oreq = req.getAuthorizationExchange().getAuthorizationRequest();
	        OAuth2AuthorizationResponse ores = req.getAuthorizationExchange().getAuthorizationResponse();
	        //MultiValueMap<String, String>
	        logger.info("11 "+entity.getBody());
	        logger.info("11 "+entity.getBody().getClass());
	        logger.info("11 "+oreq.getAuthorizationRequestUri());
	        logger.info("12 "+oreq.getAuthorizationUri());
	        logger.info("13 "+oreq.getClientId());
	        logger.info("14 "+oreq.getState());
	        logger.info("15 "+oreq.getAdditionalParameters());
	        logger.info("16 "+oreq.getAttributes());
	        logger.info("27 "+ores.getRedirectUri());
	        logger.info("28 "+ores.getState());
	        logger.info("29 "+ores.getCode());
	        
	        
	        return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
	    }

	}

	
	private static class CustomTokenResponseParametersConverter implements Converter<OAuth2AccessTokenResponse, Map<String, String>> {
		
		private Log logger = LogFactory.getLog(getClass());
		
		@Override
		public Map<String, String> convert(OAuth2AccessTokenResponse tokenResponse) {
			

			logger.info(tokenResponse.getAdditionalParameters());
			logger.info(tokenResponse.getAdditionalParameters());
			logger.info(tokenResponse.getAdditionalParameters());
			logger.info(tokenResponse.getAdditionalParameters());
			logger.info(tokenResponse.getAdditionalParameters());
			logger.info(tokenResponse.getAdditionalParameters());
			
			
			Map<String, String> parameters = new HashMap<>();

			long expiresIn = -1;
			if (tokenResponse.getAccessToken().getExpiresAt() != null) {
				expiresIn = ChronoUnit.SECONDS.between(Instant.now(), tokenResponse.getAccessToken().getExpiresAt());
			}

			parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, tokenResponse.getAccessToken().getTokenValue());
			parameters.put(OAuth2ParameterNames.TOKEN_TYPE, tokenResponse.getAccessToken().getTokenType().getValue());
			parameters.put(OAuth2ParameterNames.EXPIRES_IN, String.valueOf(expiresIn));
			if (!CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
				parameters.put(OAuth2ParameterNames.SCOPE,
						StringUtils.collectionToDelimitedString(tokenResponse.getAccessToken().getScopes(), " "));
			}
			if (tokenResponse.getRefreshToken() != null) {
				parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, tokenResponse.getRefreshToken().getTokenValue());
			}
			if (!CollectionUtils.isEmpty(tokenResponse.getAdditionalParameters())) {
				for (Map.Entry<String, Object> entry : tokenResponse.getAdditionalParameters().entrySet()) {
					parameters.put(entry.getKey(), entry.getValue().toString());
				}
			}

			return parameters;
		}
	}
	
	
	public static class CustomTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {
		
		private Log logger = LogFactory.getLog(getClass());

		
		private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<>(Arrays.asList(
				OAuth2ParameterNames.ACCESS_TOKEN,
				OAuth2ParameterNames.EXPIRES_IN,
				OAuth2ParameterNames.REFRESH_TOKEN,
				OAuth2ParameterNames.SCOPE,
				OAuth2ParameterNames.TOKEN_TYPE
		));


	    @Override
	    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
	    	
	    	for(String key : tokenResponseParameters.keySet()) {
		        logger.info(key+" = "+tokenResponseParameters.get(key));
	    	}
	    	
	    	
			String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);

			OAuth2AccessToken.TokenType accessTokenType = null;
			if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(
					tokenResponseParameters.get(OAuth2ParameterNames.TOKEN_TYPE))) {
				accessTokenType = OAuth2AccessToken.TokenType.BEARER;
			}

			long expiresIn = 0;
			if (tokenResponseParameters.containsKey(OAuth2ParameterNames.EXPIRES_IN)) {
				try {
					expiresIn = Long.parseLong(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));
				} catch (NumberFormatException ex) {
				}
			}

			Set<String> scopes = Collections.emptySet();
			if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
				String scope = tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
				scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
			}

			String refreshToken = tokenResponseParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);

			Map<String, Object> additionalParameters = new LinkedHashMap<>();
			for (Map.Entry<String, String> entry : tokenResponseParameters.entrySet()) {
				if (!TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) {
					additionalParameters.put(entry.getKey(), entry.getValue());
				}
			}

			return OAuth2AccessTokenResponse.withToken(accessToken)
					.tokenType(accessTokenType)
					.expiresIn(expiresIn)
					.scopes(scopes)
					.refreshToken(refreshToken)
					.additionalParameters(additionalParameters)
					.build();
	    }

	}    
}
