package io.github.u2ware.sample;

import java.net.URLEncoder;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.u2ware.sample.x.XPrinter;

@Controller
public class OAuth2AuthorizationController implements InitializingBean {

    protected Log logger = LogFactory.getLog(getClass());
	
	private JWKSet jwkSet;
	private NimbusJwtEncoder encoder;
	private NimbusJwtDecoder decoder;

    private @Autowired ObjectMapper objectMapper;
    private @Autowired ClientRegistrationRepository clientRegistrationRepository;
    private @Autowired OAuth2AuthorizedClientService clientService;
    private @Autowired AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

	@Override
	public void afterPropertiesSet() throws Exception {
        this.jwkSet = JWKSet.load( new ClassPathResource("JWKKeypairSet.json", this.getClass().getClassLoader()).getInputStream());
		this.encoder = new NimbusJwtEncoder(jwkSet);
		this.decoder = new NimbusJwtDecoder(jwkSet);
	}
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/token/clientRegistrations")
	public @ResponseBody Object clientRegistrations(HttpServletRequest request) throws Exception{

        InMemoryClientRegistrationRepository rr = (InMemoryClientRegistrationRepository)clientRegistrationRepository;

        UriComponents base = ServletUriComponentsBuilder.fromContextPath(request).build();
        List clients = new ArrayList();
        rr.forEach((r)->{
            Map client = new HashMap();
            client.put("clientRegistrationId", r.getRegistrationId());
            client.put("clientName", r.getClientName());
            client.put("authorizationUri", base+"/login/"+r.getRegistrationId());
            client.put("userJwtUri"     , base+"/user/info");
            client.put("userInfoUri"    , r.getProviderDetails().getUserInfoEndpoint().getUri());

            clients.add(client);
        });
        return clients;
    }

	@GetMapping("/token/jwks.json")
	public @ResponseBody Map<String, Object> jwks() {
		return jwkSet.toJSONObject(true);
	}

    @RequestMapping(value = "/token/callback")
    public @ResponseBody Object callback(HttpServletRequest request) {
    	return ServletUriComponentsBuilder.fromRequest(request).build().getQueryParams().toSingleValueMap();
    }
	
	@GetMapping("/user/info")
	public @ResponseBody ResponseEntity<?> info(HttpServletRequest request) throws Exception{

        String bearerTokenValue = extractHeaderToken(request);
        try{
            Jwt jwt = decoder.decode(bearerTokenValue);
            return ResponseEntity.ok(jwt);
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


//	@PostMapping("/token/encode")
//	public @ResponseBody Object encode(@RequestBody Jwt jwt) throws Exception{
//        return encoder.encode(jwt);
//    }
//
//	@PostMapping("/token/decode")
//	public @ResponseBody Object decode(@RequestBody String token) throws Exception{
//        return decoder.decode(token);
//    }

    @RequestMapping("/login")
    public String login(Model model) {
    	
    	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {

            // OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            // OAuth2User oauthUser = oauthToken.getPrincipal();
            // model.addAttribute("oauthToken", oauthToken);

            return "logonPage";

        } else {
            model.addAttribute("registrations", clientRegistrationRepository);
            return "loginPage";
        }
    }
    
    @RequestMapping(value = "/login/{clientRegistrationId}")
    public String login(
            HttpServletRequest request, 
            @PathVariable("clientRegistrationId") String clientRegistrationId,
            @RequestParam(value = "callback_uri", required = false) String callback) 
            throws Exception{

        XPrinter.print("login: ", request);

        UriComponents redirect = UriComponentsBuilder
            .fromPath(OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
            .pathSegment(clientRegistrationId)
            .queryParam("callback_uri", callback)
            .build();
        logger.info(redirect);
        return "redirect:" + redirect;
    }
    
    @RequestMapping(value = "/logon")
    public String logon(HttpServletRequest request, Model model, Principal principal,
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {

        XPrinter.print("logon: ", request);

        String clientRegistrationId = clientRegistrationId(oauth2User, authorizedClient);
        String principalName = principalName(oauth2User, authorizedClient);
        String accessToken = accessToken(oauth2User, authorizedClient);
        String idToken = idToken(oauth2User, authorizedClient);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("clientRegistrationId", clientRegistrationId);
        params.add("principalName", principalName);
        params.add("accessToken", accessToken);
        params.add("idToken", idToken);

        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestRepository.loadAuthorizationRequest(request);
        if (StringUtils.isEmpty(authorizationRequest)) {
            model.addAttribute("oauth2User", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(oauth2User));
            model.addAttribute("authorizedClient", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authorizedClient));
            model.addAttribute("paremeters", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params.toSingleValueMap()));
            return login(model);
        }

        String url = authorizationRequest.getRedirectUri();
        UriComponents redirect = UriComponentsBuilder.fromUriString(url).queryParams(params).build();
        logger.info("redirect: "+redirect.toString().length());
        return "redirect:" + redirect;
    }

    private String clientRegistrationId(OAuth2User oauth2User, OAuth2AuthorizedClient authorizedClient)throws Exception{
        return authorizedClient.getClientRegistration().getRegistrationId();
    }
   private String principalName(OAuth2User oauth2User, OAuth2AuthorizedClient authorizedClient) throws Exception{
       return URLEncoder.encode(authorizedClient.getPrincipalName(), "UTF-8");
   }
    private String accessToken(OAuth2User oauth2User, OAuth2AuthorizedClient authorizedClient)throws Exception{
        return authorizedClient.getAccessToken().getTokenValue();
    }
//    private String idToken(OAuth2User oauth2User, OAuth2AuthorizedClient authorizedClient)throws Exception{
//        if (ClassUtils.isAssignableValue(DefaultOidcUser.class, oauth2User)) {
//            DefaultOidcUser oidcUser = (DefaultOidcUser) oauth2User;
//            return oidcUser.getIdToken().getTokenValue();
//        }
//        return null;
//    }
    private String idToken(OAuth2User oauth2User, OAuth2AuthorizedClient authorizedClient) throws Exception{

        String tokenValue = authorizedClient.getAccessToken().getTokenValue();
        Instant issuedAt = authorizedClient.getAccessToken().getIssuedAt();
        Instant expiresAt = authorizedClient.getAccessToken().getExpiresAt();
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "RS256");

        Map<String, Object> claims = new HashMap<>(oauth2User.getAttributes());
        claims.put("clientRegistrationId", authorizedClient.getClientRegistration().getRegistrationId());
        claims.put("principalName", URLEncoder.encode(authorizedClient.getPrincipalName(), "UTF-8"));
        //CommonOAuth2Provider d;
//      claims.put("principalName", authorizedClient.getPrincipalName());
//        logger.info("authorizedClient------------------------------------------------------");
//        logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(authorizedClient));
//        logger.info("oauth2User ------------------------------------------------------");
//        logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(oauth2User));
//        logger.info("---------------------------------------------------------------");
        Jwt jwt = new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
        String token = encoder.encode(jwt);
//        logger.info("JWT Encode Token------------------------------------------------------");
//        logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jwt));
//        logger.info("JWT Decode Token------------------------------------------------------");
//        logger.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(decoder.decode(token)));
//        logger.info("---------------------------------------------------------------");
        
        return token;
    }


    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/user/oauth2user")
    public @ResponseBody OAuth2User oauth2User(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }

    @RequestMapping(value="/user/oauth2authorizedClient")
    public @ResponseBody OAuth2AuthorizedClient oauth2User(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient){
        return authorizedClient;
    }
    
    @RequestMapping(value="/user")
    public @ResponseBody Map<String,Object> oauth2User() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // logger.info("---------------------------");
        // logger.info("/oauth2: "+authentication.hashCode()+" "+authentication.getClass());
        // logger.info(clientService.getClass()); //InMemoryOAuth2AuthorizedClientService
        // logger.info(clientService);
        // logger.info(clientRepository.getClass()); //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
        // logger.info(clientRepository);
        // logger.info("---------------------------");

        Map<String,Object> contents = new HashMap<>();
        contents.put(Authentication.class.getName(), authentication);

        if (ClassUtils.isAssignableValue(OAuth2AuthenticationToken.class, authentication)) {

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            ///////////////////////////////////////////////////////
            //
            ///////////////////////////////////////////////////////
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();

            OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(clientRegistrationId, principalName);
            // OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            // String token = accessToken.getTokenValue();
            contents.put(OAuth2AuthorizedClient.class.getName(), authorizedClient);

            ///////////////////////////////////////////////////////
            //
            ///////////////////////////////////////////////////////
            OAuth2User oauth2User = oauthToken.getPrincipal();

            if (ClassUtils.isAssignableValue(DefaultOidcUser.class, oauth2User)) {
                DefaultOidcUser oidcUser = (DefaultOidcUser)oauth2User;
                //String jwtToken = oidcUser.getIdToken().getTokenValue();
                contents.put(DefaultOidcUser.class.getName(), oidcUser);
            }else{
                contents.put(OAuth2User.class.getName(), oauth2User);
            }
        }

        // DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
        // ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        // Instant issuedAt = Instant.now();
        // Instant expiresAt = Instant.from(issuedAt).plusSeconds(60);
        // OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, issuedAt, expiresAt);
        // OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
        // OAuth2User oauth2User = userService.loadUser(userRequest);
        // //OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(clientRegistrationId, oauth2User.getName());

        return contents;
    }
}