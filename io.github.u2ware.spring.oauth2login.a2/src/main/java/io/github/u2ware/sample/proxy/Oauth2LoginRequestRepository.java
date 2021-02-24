package io.github.u2ware.sample.proxy;

import static io.github.u2ware.sample.ApplicationSecurityConfig.OAUTH2_CALLBACK_PARAM;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

@Component
public class Oauth2LoginRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>{
	
	private Log logger = LogFactory.getLog(getClass());

    private Map<String, OAuth2AuthorizationRequest> authorizationRequests = new ConcurrentHashMap<>();
    private Map<String, String> callbackRequests = new ConcurrentHashMap<>();

    //////////////////////////////////////////////////////////////////////////
    //
    //////////////////////////////////////////////////////////////////////////    
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
            return;
        }
        
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");

        String key = resolveOAuth2AuthorizationRequestKey(request, state);
        authorizationRequests.put(key, authorizationRequest);

        String callback = request.getParameter(OAUTH2_CALLBACK_PARAM);
        if(StringUtils.hasText(callback)){
            callbackRequests.put(key, request.getParameter(OAUTH2_CALLBACK_PARAM));        
        }
        logger.info("save: "+key);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {

        Assert.notNull(request, "request cannot be null");
        
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (state == null) {
            return null;
        }

        String key = resolveOAuth2AuthorizationRequestKey(request, state);
        OAuth2AuthorizationRequest authorizationRequest =  authorizationRequests.remove(key);


        String callback = callbackRequests.remove(key);
        if(StringUtils.hasText(callback)){
            WebUtils.setSessionAttribute(request, OAUTH2_CALLBACK_PARAM, callback);
            logger.info("add callback: "+callback);
        }
        logger.info("remove: "+key);
        return authorizationRequest;
    }




    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (state == null) {
            String callback = (String)WebUtils.getSessionAttribute(request, OAUTH2_CALLBACK_PARAM);
            logger.info("get callback: "+callback);
            if(StringUtils.hasText(callback)){
            	//Return Fake OAuth2AuthorizationRequest
                return OAuth2AuthorizationRequest
                		.authorizationCode()
                        .authorizationRequestUri(callback)
                        .authorizationUri(callback)
                        .clientId(callback)
                        .redirectUri(callback).build();
            }else{
                return null;
            }
        }

        String key = resolveOAuth2AuthorizationRequestKey(request, state);
        OAuth2AuthorizationRequest authorizationRequest =  authorizationRequests.get(key);
        logger.info("load: "+key);
        return authorizationRequest;
    }


    private String resolveOAuth2AuthorizationRequestKey(HttpServletRequest request, String state) {
        UriComponents c = UriComponentsBuilder.fromPath(request.getRequestURI()).build();
        String lastSegment = c.getPathSegments().stream().reduce((first, second) -> second).orElse(null);
        return lastSegment+"/"+state;
    }
}
