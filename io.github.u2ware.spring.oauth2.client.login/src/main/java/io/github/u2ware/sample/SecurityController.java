package io.github.u2ware.sample;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class SecurityController {

    protected Log logger = LogFactory.getLog(getClass());


    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientRepository clientRepository;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    private DefaultOAuth2UserService userService = new DefaultOAuth2UserService();



    @GetMapping("/login")
    public String home(Model model) {

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

    @GetMapping(value = "/login/{clientRegistrationId}")
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
        logger.info(redirect);
        return "redirect:" + redirect;
    }

    @GetMapping(value="/logout/{clientRegistrationId}")
    public @ResponseBody ResponseEntity<Object> logoff(
            @RequestHeader("Authorization") String principalName, 
            @PathVariable("clientRegistrationId")String clientRegistrationId) 
            throws Exception{

        clientService.removeAuthorizedClient(clientRegistrationId, principalName);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/info/{clientRegistrationId}")
    public @ResponseBody ResponseEntity<Object> info(
            @RequestHeader("Authorization") String principalName, 
            @PathVariable("clientRegistrationId")String clientRegistrationId) 
            throws Exception {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(clientRegistrationId, principalName);
        if(authorizedClient == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
        OAuth2User oauth2User = userService.loadUser(userRequest);
        
        return ResponseEntity.ok(oauth2User);
    }


    @RequestMapping(value = "/logon")
    public String logon(
            HttpServletRequest request, 
            Model model, 
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient)
            throws Exception {

        Object callback = request.getSession().getAttribute(getClass().getName());
        if(StringUtils.isEmpty(callback)){
            return home(model);
        }

        String token = "";
        String jwtToken = "";
        String principalName = "";
        String clientRegistrationId = "";
        token = authorizedClient.getAccessToken().getTokenValue();
        principalName = authorizedClient.getPrincipalName();
        clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
        if (ClassUtils.isAssignableValue(DefaultOidcUser.class, oauth2User)) {
            DefaultOidcUser oidcUser = (DefaultOidcUser)oauth2User;
            jwtToken = oidcUser.getIdToken().getTokenValue();
        }

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

    
    ////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////
    @GetMapping(value="/oauth2/user")
    public @ResponseBody OAuth2User oauth2User(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }

    @GetMapping(value="/oauth2/authorizedClient")
    public @ResponseBody OAuth2AuthorizedClient oauth2User(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient){
        return authorizedClient;
    }

    @GetMapping(value="/oauth2")
    public @ResponseBody Map<String,Object> oauth2User() {

        Map<String,Object> contents = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        contents.put(Authentication.class.getName(), authentication);

        logger.info("---------------------------");
        logger.info(clientService.getClass()); //InMemoryOAuth2AuthorizedClientService
        logger.info(clientService);
        logger.info(clientRepository.getClass()); //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
        logger.info(clientRepository);
        logger.info("---------------------------");

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