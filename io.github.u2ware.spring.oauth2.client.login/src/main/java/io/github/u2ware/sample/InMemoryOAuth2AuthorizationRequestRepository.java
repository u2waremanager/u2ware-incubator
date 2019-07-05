package io.github.u2ware.sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.u2ware.sample.x.XPrinter;

//HttpSessionOAuth2AuthorizationRequestRepository
public class InMemoryOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    protected Log logger = LogFactory.getLog(getClass());

    private static final String CALLBACK_URI = "callback_uri";
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

        String callback = request.getParameter(CALLBACK_URI);
        if(StringUtils.hasText(callback)){
            callbackRequests.put(key, request.getParameter(CALLBACK_URI));        
        }
        logger.info("save: "+key);
        XPrinter.print("save: ", request);
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
            request.getSession().setAttribute(CALLBACK_URI, callback);
        }

        logger.info("remove: "+key);
        XPrinter.print("remove: ", request);
        return authorizationRequest;
    }




    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (state == null) {
            String callback = (String)request.getSession().getAttribute(CALLBACK_URI);
            if(StringUtils.hasText(callback)){
                return OAuth2AuthorizationRequest.authorizationCode()
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
        XPrinter.print("load: ", request);
        return authorizationRequest;
    }


    private String resolveOAuth2AuthorizationRequestKey(HttpServletRequest request, String state) {
        UriComponents c = UriComponentsBuilder.fromPath(request.getRequestURI()).build();
        String lastSegment = c.getPathSegments().stream().reduce((first, second) -> second).orElse(null);
        return lastSegment+"/"+state;
    }
}