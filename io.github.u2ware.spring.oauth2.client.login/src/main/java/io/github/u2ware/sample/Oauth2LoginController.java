package io.github.u2ware.sample;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.u2ware.sample.x.NimbusDecoder;
import io.github.u2ware.sample.x.NimbusEncoder;

@Controller
public class Oauth2LoginController implements InitializingBean{

    protected Log logger = LogFactory.getLog(getClass());
	
    private @Autowired ClientRegistrationRepository clientRegistrationRepository;
    private @Autowired OAuth2AuthorizedClientService clientService;
    
    @RequestMapping("/login")
    public String login(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            model.addAttribute("oauthToken", oauthToken);
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
            @RequestParam(value = "callback", required = false) String callback) 
            throws Exception{

        if(StringUtils.hasText(callback)){
            request.getSession().setAttribute(getClass().getName(), callback);
        }

        UriComponents redirect = UriComponentsBuilder
            .fromPath(OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI)
            .pathSegment(clientRegistrationId).build();
        logger.info("/login/"+clientRegistrationId +" ["+callback+"]");
        return "redirect:" + redirect;
    }
    
    @RequestMapping(value = "/logon")
    public String logon(HttpServletRequest request, Model model, Principal principal,
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {

        Object callback = request.getSession().getAttribute(getClass().getName());
        if (StringUtils.isEmpty(callback)) {
            return login(model);
        }
        
        logger.info("---------------------------");
        logger.info("/logon");
        logger.info("/logon: " + principal.hashCode() + " " + principal.getClass());
        logger.info("/logon: " + oauth2User.hashCode() + " " + oauth2User.getClass());
        logger.info("/logon: " + authorizedClient.hashCode() + " " + authorizedClient.getClass());
        logger.info("---------------------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("---------------------------");
        logger.info("/logon: " + authentication.hashCode() + " " + authentication.getClass());
        logger.info("/logon: " + authentication.getPrincipal().hashCode() + " " + authentication.getPrincipal().getClass());
        logger.info("/logon: " + authentication.getDetails().hashCode() + " " + authentication.getDetails().getClass());
        logger.info("/logon: " + authentication.getAuthorities().hashCode() + " " + authentication.getAuthorities().getClass());
        logger.info("---------------------------");
        
        logger.info("---------------------------");
        logger.info("/logon: " + authorizedClient.getAccessToken().hashCode()+" "+authorizedClient.getAccessToken().getClass());
        logger.info("---------------------------");
        

        
        
        // JwtAccessTokenConverter converter = new JwtAccessTokenConverter();


        String token = "";
        String jwtToken = "";
        String principalName = "";
        String clientRegistrationId = "";
        token = authorizedClient.getAccessToken().getTokenValue();
        principalName = authorizedClient.getPrincipalName();
        clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
        
        
        OAuth2AuthorizedClient OAuth2AuthorizedClient = clientService.loadAuthorizedClient(clientRegistrationId, principalName);

        
        //clientService.saveAuthorizedClient(OAuth2AuthorizedClient, principal);
        
        
        
        
        
        if (ClassUtils.isAssignableValue(DefaultOidcUser.class, oauth2User)) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) oauth2User;
            jwtToken = oidcUser.getIdToken().getTokenValue();
        }

        // JwtDecoder d = JwtDecoders.fromOidcIssuerLocation("/aaaaaaaaa/");
        // Jwt xToken = d.decode(token);

        UriComponents redirect = UriComponentsBuilder
            .fromUriString(callback.toString())
            .queryParam("principalName", URLEncoder.encode(principalName,"UTF-8"))
            .queryParam("clientRegistrationId", clientRegistrationId)
            .queryParam("token", token)
            .queryParam("jwtToken", jwtToken)
            .build();

        logger.info(redirect);
        return "redirect:" + redirect;
    }

    
    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
    @RequestMapping(value="/oauth2/user")
    public @ResponseBody OAuth2User oauth2User(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }

    @RequestMapping(value="/oauth2/authorizedClient")
    public @ResponseBody OAuth2AuthorizedClient oauth2User(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient){
        return authorizedClient;
    }
    
    @RequestMapping(value="/oauth2")
    public @ResponseBody Map<String,Object> oauth2User() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        
        logger.info("---------------------------");
        logger.info("/oauth2: "+authentication.hashCode()+" "+authentication.getClass());
        // logger.info(clientService.getClass()); //InMemoryOAuth2AuthorizedClientService
        // logger.info(clientService);
        // logger.info(clientRepository.getClass()); //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
        // logger.info(clientRepository);
        logger.info("---------------------------");

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
    
    ////////////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////////////
	private NimbusEncoder encoder;// = new NimbusJwtEncoder(JWKKeypairSet.getFile());
	private NimbusDecoder decoder;// = new NimbusJwtDecoder(JWKKeypairSet.getFile());
	
	@Override
	public void afterPropertiesSet() throws Exception {
		encoder = new NimbusEncoder(new ClassPathResource("JWKKeypairSet.json").getFile());
	}

	
	@GetMapping("/.well-known/jwks.json")
	public @ResponseBody Map<String, Object> getKey() {
		return encoder.getJWKSet().toJSONObject(true);
	}
}
