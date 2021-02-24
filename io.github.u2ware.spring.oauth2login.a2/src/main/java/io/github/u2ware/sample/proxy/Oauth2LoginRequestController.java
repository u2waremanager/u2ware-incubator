package io.github.u2ware.sample.proxy;

import static io.github.u2ware.sample.ApplicationSecurityConfig.AUTHORIZATION_ENDPOINT_BASE_URI;
import static io.github.u2ware.sample.ApplicationSecurityConfig.OAUTH2_CALLBACK_PARAM;
import static io.github.u2ware.sample.ApplicationSecurityConfig.OAUTH2_CALLBACK_URI;
import static io.github.u2ware.sample.ApplicationSecurityConfig.OAUTH2_REGISTRATION_PARAM;
import static io.github.u2ware.sample.ApplicationSecurityConfig.OAUTH2_REGISTRATION_URI;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class Oauth2LoginRequestController {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	@Autowired
    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    
	@GetMapping(value=OAUTH2_REGISTRATION_URI)
	public @ResponseBody List<Map<String,String>> oauth2(HttpServletRequest request) {
		
		@SuppressWarnings("unchecked")
		Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>)clientRegistrationRepository;
		
        List<Map<String,String>> clients = new ArrayList<>();
        clientRegistrations.forEach(clientRegistration->{

            UriComponents uri = ServletUriComponentsBuilder
            		.fromContextPath(request)
            		.path(OAUTH2_REGISTRATION_URI)
            		.queryParam(OAUTH2_REGISTRATION_PARAM, clientRegistration.getRegistrationId())
               		.queryParam(OAUTH2_CALLBACK_PARAM, "")
               		.build();
        	
            Map<String,String> client = new HashMap<>();
            client.put("id", clientRegistration.getRegistrationId());
            client.put("name", clientRegistration.getClientName());
            client.put("uri", uri.toString());
            clients.add(client);
        });
		return clients;
	}
	
	

	@GetMapping(value=OAUTH2_REGISTRATION_URI, params = {OAUTH2_REGISTRATION_PARAM,OAUTH2_CALLBACK_PARAM})
	public String oauth2Login(
			@RequestParam(OAUTH2_REGISTRATION_PARAM) String registrationId, 
			@RequestParam(OAUTH2_CALLBACK_PARAM) String callback) {

		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
		if(clientRegistration == null) throw new HttpServerErrorException(HttpStatus.NOT_FOUND);

        UriComponents redirect = UriComponentsBuilder
        		.fromPath(AUTHORIZATION_ENDPOINT_BASE_URI)
        		.pathSegment(registrationId)
        		.queryParam(OAUTH2_CALLBACK_PARAM, callback)
        		.build();
		
        logger.info("redirect: "+redirect);
        return "redirect:" + redirect;
	}
	
	

	@GetMapping(value=OAUTH2_CALLBACK_URI)
	public String oauth2LoginCallback(HttpServletRequest request, @AuthenticationPrincipal OAuth2User oauth2User, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestRepository.loadAuthorizationRequest(request);
        
//        authorizedClient.getAccessToken().;
//        authorizedClient.getRefreshToken();
//        authorizedClient.getPrincipalName();
//        authorizedClient.g
//        authorizedClient.getClientRegistration().getScopes()
        if(authorizationRequest == null) {
            UriComponents forward = ServletUriComponentsBuilder
            		.fromContextPath(request)
            		.path(OAUTH2_CALLBACK_URI+"/oauth2.json")
            		.build();
            logger.info(forward);
            return "redirect:" + forward;
        }
        
        UriComponents redirect = UriComponentsBuilder
        		.fromUriString(authorizationRequest.getRedirectUri())
        		.queryParams(convert(authorizedClient))
        		.queryParams(convert(oauth2User))
        		.build();
        logger.info("redirect: "+redirect);
        return "redirect:" + redirect;
	}

	@GetMapping(value=OAUTH2_CALLBACK_URI+"/oauth2.json")
	public @ResponseBody Object oauth2LoginCallbackDefault(@AuthenticationPrincipal OAuth2User oauth2User, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		Map<String,Object> response = new HashMap<String,Object>();
//		response.putAll(convert(authorizedClient));
//		return response;
		response.put("oauth2User", oauth2User);
		response.put("oauth2AuthorizedClient", authorizedClient);
		return response;//convert(authorizedClient);
	}
		

	public MultiValueMap<String,String> convert(OAuth2User oauth2User){
		
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		
		if(ClassUtils.isAssignableValue(DefaultOidcUser.class, oauth2User)) {
			DefaultOidcUser oidcUser = (DefaultOidcUser)oauth2User;
			parameters.add("id_token", oidcUser.getIdToken().getTokenValue());
		}
//		Object r = oauth2User.getAttribute("idToken");
//		logger.info(oauth2User.getClass());
//		logger.info(oauth2User.getAttributes().keySet());
//		logger.info(oauth2User.getAttributes());
//		logger.info(r.getClass());
		
		return parameters;
	}

	
	public MultiValueMap<String,String> convert(OAuth2AuthorizedClient client){
		
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		long expiresIn = -1;
		if (client.getAccessToken().getExpiresAt() != null) {
			expiresIn = ChronoUnit.SECONDS.between(Instant.now(), client.getAccessToken().getExpiresAt());
		}

		parameters.add(OAuth2ParameterNames.ACCESS_TOKEN, client.getAccessToken().getTokenValue());
		parameters.add(OAuth2ParameterNames.TOKEN_TYPE, client.getAccessToken().getTokenType().getValue());
		parameters.add(OAuth2ParameterNames.EXPIRES_IN, String.valueOf(expiresIn));
		if (!CollectionUtils.isEmpty(client.getAccessToken().getScopes())) {
			parameters.add(OAuth2ParameterNames.SCOPE,
					StringUtils.collectionToDelimitedString(client.getAccessToken().getScopes(), " "));
		}
		if (client.getRefreshToken() != null) {
			parameters.add(OAuth2ParameterNames.REFRESH_TOKEN, client.getRefreshToken().getTokenValue());
		}
//		if (!CollectionUtils.isEmpty(tokenResponse.getAdditionalParameters())) {
//			for (Map.Entry<String, Object> entry : tokenResponse.getAdditionalParameters().entrySet()) {
//				parameters.put(entry.getKey(), entry.getValue().toString());
//			}
//		}
		
		parameters.add(OAuth2ParameterNames.USERNAME, client.getPrincipalName());
		parameters.add("client_registration_name", client.getClientRegistration().getClientName());
		parameters.add("user_info_endpoint", client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
		
		return parameters;
		
	}
}
