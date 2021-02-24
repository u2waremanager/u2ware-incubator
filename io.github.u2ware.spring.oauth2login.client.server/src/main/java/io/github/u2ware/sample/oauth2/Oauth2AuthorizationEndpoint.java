package io.github.u2ware.sample.oauth2;

import static io.github.u2ware.sample.ApplicationSecurityConfig.JWT_JWKS_JSON;
import static io.github.u2ware.sample.ApplicationSecurityConfig.JWT_USER_INFO;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * authorization 권한 / 인가  /오쏘로제이션/
 * 
 * @author u2ware
 */
@Controller
public class Oauth2AuthorizationEndpoint {

	@Autowired
    private OAuth2AuthorizationService oauth2AuthorizationService;


	@GetMapping(JWT_JWKS_JSON)
	public @ResponseBody Map<String, Object> jwks() {
		return oauth2AuthorizationService.jwks();
	}
	
	
	@GetMapping(JWT_USER_INFO)
	public @ResponseBody ResponseEntity<?> info(HttpServletRequest request) throws Exception{

        String bearerTokenValue = extractHeaderToken(request);
        try{
            return ResponseEntity.ok(oauth2AuthorizationService.decode(bearerTokenValue));
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
	
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	
	
	@GetMapping(JWT_USER_INFO+"/{registrationId}")
	public @ResponseBody ResponseEntity<?> info(HttpServletRequest request, @PathVariable String registrationId) throws Exception{

		String userInfoEndpoint = clientRegistrationRepository.findByRegistrationId(registrationId).getProviderDetails().getUserInfoEndpoint().getUri();
        String accessToken = extractHeaderToken(request);
		String tokenType = "Bearer";
		
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenType+" "+accessToken);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);
        Map<String, String> params = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> res = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, httpEntity, Map.class, params);
		@SuppressWarnings("rawtypes")
		Map userInfo = res.getBody();
		
        return ResponseEntity.ok(userInfo);
	}

}
