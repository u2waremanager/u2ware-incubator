package io.github.u2ware.sample.x;

import java.net.URLEncoder;
import java.security.Principal;
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

public class SecurityController {

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired ClientRegistrationRepository clientRegistrationRepository;
    private @Autowired OAuth2AuthorizedClientRepository clientRepository;
    private @Autowired OAuth2AuthorizedClientService clientService;
    private DefaultOAuth2UserService userService = new DefaultOAuth2UserService();

//    private @Autowired NimbusJwkSupport nimbusJwkSupport;
//    
//    @GetMapping("/.well-known/jwks.json")
//    @ResponseBody
//    public Map<String, Object> getKey() {
//        return nimbusJwkSupport.getPublicKey();
//    }
    
    
    //////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////
    @RequestMapping(value="/logout/{clientRegistrationId}")
    public @ResponseBody ResponseEntity<Object> logout(
            @RequestHeader("Authorization") String principalName, 
            @PathVariable("clientRegistrationId")String clientRegistrationId) 
            throws Exception{

        logger.info("/logout/"+clientRegistrationId +" ["+principalName+"]");
        clientService.removeAuthorizedClient(clientRegistrationId, principalName);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/info/{clientRegistrationId}/{principalName}")
    public @ResponseBody ResponseEntity<Object> info2(
            @PathVariable("principalName") String principalName, 
            @PathVariable("clientRegistrationId")String clientRegistrationId) 
            throws Exception {
            
        return info1(principalName, clientRegistrationId);
    }



    @RequestMapping(value="/info/{clientRegistrationId}")
    public @ResponseBody ResponseEntity<Object> info1(
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

//        try{
//            OAuth2LoginAuthenticationFilter s1 = new OAuth2LoginAuthenticationFilter(clientRegistrationRepository, clientService);
//            //(XAuthorizationRequestRepository.get)
//            OAuth2LoginAuthenticationProvider s2; //(XOAuth2AuthorizedClientRepository.save)
//            OidcAuthorizationCodeAuthenticationProvider s3;
//            HttpSessionSecurityContextRepository s;
//            OidcUserService oidcUserService = new OidcUserService();
//            
//            // private OidcUserService userService = new DefaultOAuth2UserService();
//            // boolean oidcAuthenticationProviderEnabled = ClassUtils.isPresent("org.springframework.security.oauth2.jwt.JwtDecoder", this.getClass().getClassLoader());
//
//
//            OAuth2AuthorizationRequest authorizationRequest = null;
//            OAuth2AuthorizationResponse authorizationResponse = null;
//            OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);
//            OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest = new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange);
//            
//            DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
//            OAuth2AccessTokenResponse accessTokenResponse = accessTokenResponseClient.getTokenResponse(authorizationGrantRequest);
//
//
//             OidcIdToken idToken = null;
//             OidcUserRequest r = new OidcUserRequest(clientRegistration, accessToken, idToken);
//             OidcUser oidcUser = oidcUserService.loadUser(r);
//
//        }catch(Exception e){
//
//        }
        
        logger.info("/info/"+clientRegistrationId +" ["+principalName+"]");
        Map<String,Object> content = new HashMap<>();
        content.put(OAuth2AuthorizedClient.class.getName(), authorizedClient);
        content.put(OAuth2User.class.getName(), oauth2User);
        return ResponseEntity.ok(content);
    }
    
    ////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////
    // @RequestMapping(value="/info")
    // public @ResponseBody ResponseEntity<Object> info()throws Exception {
    //     return ResponseEntity.ok(clientService.getAll().values());
    // }

}