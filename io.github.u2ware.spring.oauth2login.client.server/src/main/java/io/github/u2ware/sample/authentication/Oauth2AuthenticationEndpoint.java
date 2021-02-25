package io.github.u2ware.sample.authentication;

import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.AUTHORIZATION_ENDPOINT_BASE_URI;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.LOGIN_CALLBACK_PARAM;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.LOGIN_PROVIDER_PARAM;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.OAUTH2_LOGIN_URI;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.OAUTH2_LOGOFF_URI;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.OAUTH2_LOGON_URI;
import static io.github.u2ware.sample.ApplicationOAuth2LoginConfig.OAUTH2_LOGOUT_URI;

import java.net.URLEncoder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.u2ware.sample.authorization.OAuth2AuthorizationService;

/**
 * authentication 사용 or 입장 or 로그인 / 인증 /어쎈티케이션/
 * 
 * @author u2ware
 */
@Controller
public class Oauth2AuthenticationEndpoint {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	@Autowired
    private Oauth2AuthenticationRepository oauth2AuthenticationRepository;

	@Autowired
    private OAuth2AuthorizationService oauth2AuthorizationService;

    
	
	
	@GetMapping(value=OAUTH2_LOGIN_URI)
	public @ResponseBody List<Map<String,String>> oauth2(HttpServletRequest request) {
		
		@SuppressWarnings("unchecked")
		Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>)clientRegistrationRepository;
		
        List<Map<String,String>> clients = new ArrayList<>();
        clientRegistrations.forEach(clientRegistration->{

            UriComponents uri = ServletUriComponentsBuilder
            		.fromContextPath(request)
            		.path(OAUTH2_LOGIN_URI)
            		.queryParam(LOGIN_PROVIDER_PARAM, clientRegistration.getRegistrationId())
               		.queryParam(LOGIN_CALLBACK_PARAM, "")
               		.build();
        	
            Map<String,String> client = new HashMap<>();
            //client.put("provider", clientRegistration.getRegistrationId());
            client.put("name", clientRegistration.getClientName());
            client.put("uri", uri.toString());
            clients.add(client);
        });
		return clients;
	}
	

	@GetMapping(value=OAUTH2_LOGIN_URI, params = {LOGIN_PROVIDER_PARAM,LOGIN_CALLBACK_PARAM})
	public String oauth2Login(
			@RequestParam(LOGIN_PROVIDER_PARAM) String provider, 
			@RequestParam(LOGIN_CALLBACK_PARAM) String callback) {
		
		
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);
		if(clientRegistration == null) throw new HttpServerErrorException(HttpStatus.NOT_FOUND);

        UriComponents redirect = UriComponentsBuilder
        		.fromPath(AUTHORIZATION_ENDPOINT_BASE_URI)
        		.pathSegment(provider)
        		.queryParam(LOGIN_CALLBACK_PARAM, callback)
        		.build();
		
        logger.info("OAuth2 Login: "+redirect);
        return "redirect:" + redirect;
	}
	
	@GetMapping(value=OAUTH2_LOGOUT_URI)
	public String oauth2Logout(HttpServletRequest request) {
        UriComponents logout = ServletUriComponentsBuilder
        		.fromContextPath(request)
        		.pathSegment("logout")
        		.build();
        logger.info("OAuth2 Logout: "+logout);
        return "redirect:" + logout;
	}
	
	@GetMapping(value=OAUTH2_LOGOFF_URI)
	public @ResponseBody Object oauth2Logoff() {
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("logout", "success");
		return response;
	}


	@GetMapping(value=OAUTH2_LOGON_URI)
	public String oauth2Logon(HttpServletRequest request, 
			@AuthenticationPrincipal OAuth2User oauth2User, 
			@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception{
		
        OAuth2AuthorizationRequest authorizationRequest = oauth2AuthenticationRepository.loadAuthorizationRequest(request);
        if(authorizationRequest == null) {
            UriComponents forward = ServletUriComponentsBuilder
            		.fromContextPath(request)
            		.path(OAUTH2_LOGON_URI+"/"+LOGIN_CALLBACK_PARAM) // goto oauth2LogonDefault()
            		.build();
            logger.info("OAuth2 Logon: "+forward);
            return "redirect:" + forward;
        }
        
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(authorizationRequest.getRedirectUri());
        
        
		builder.queryParam(OAuth2ParameterNames.ACCESS_TOKEN, authorizedClient.getAccessToken().getTokenValue());
		builder.queryParam(OAuth2ParameterNames.TOKEN_TYPE, authorizedClient.getAccessToken().getTokenType().getValue());
		builder.queryParam(OAuth2ParameterNames.USERNAME, URLEncoder.encode(authorizedClient.getPrincipalName(), "UTF-8"));
		if (!CollectionUtils.isEmpty(authorizedClient.getAccessToken().getScopes())) {
			builder.queryParam(OAuth2ParameterNames.SCOPE, StringUtils.collectionToDelimitedString(authorizedClient.getAccessToken().getScopes(), " "));
		}
		if (authorizedClient.getRefreshToken() != null) {
			builder.queryParam(OAuth2ParameterNames.REFRESH_TOKEN, authorizedClient.getRefreshToken().getTokenValue());
		}
		long expiresIn = -1;
		if (authorizedClient.getAccessToken().getExpiresAt() != null) {
			expiresIn = ChronoUnit.SECONDS.between(Instant.now(), authorizedClient.getAccessToken().getExpiresAt());
		}
		builder.queryParam(OAuth2ParameterNames.EXPIRES_IN, String.valueOf(expiresIn));
		
		
		builder.queryParam("client_registration_id", authorizedClient.getClientRegistration().getRegistrationId());
		builder.queryParam("client_registration_name", authorizedClient.getClientRegistration().getClientName());
		builder.queryParam("access_token_user_info", authorizedClient.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
		builder.queryParam("id_token", oauth2AuthorizationService.encode(authorizedClient, oauth2User) );
		builder.queryParam("id_token_user_info", ServletUriComponentsBuilder.fromContextPath(request).path("/user/info").build());
		
		
		
		
		
        UriComponents redirect = builder.build();
        logger.info("OAuth2 Logon: "+redirect);
        return "redirect:" + redirect;
	}
	
	@GetMapping(value=OAUTH2_LOGON_URI+"/"+LOGIN_CALLBACK_PARAM)
	public @ResponseBody Object oauth2LogonDefault(
			@AuthenticationPrincipal OAuth2User oauth2User, 
			@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception{
		
		String userInfoEndpoint = authorizedClient.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		String tokenType = authorizedClient.getAccessToken().getTokenType().getValue();

		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenType+" "+accessToken);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
        Map<String, String> params = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> res = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, httpEntity, Map.class, params);
		@SuppressWarnings("rawtypes")
		Map userInfo = res.getBody();
		
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("oauth2User", oauth2User);
		response.put("oauth2AuthorizedClient", authorizedClient);
		response.put("userInfo", userInfo);
		return response;
	}

}
