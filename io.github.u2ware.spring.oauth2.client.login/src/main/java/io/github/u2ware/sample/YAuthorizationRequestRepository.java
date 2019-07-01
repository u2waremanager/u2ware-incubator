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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

//HttpSessionOAuth2AuthorizationRequestRepository
public class YAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    protected Log logger = LogFactory.getLog(getClass());

    private Map<String, OAuth2AuthorizationRequest> authorizationRequests = new ConcurrentHashMap<>();

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (state == null) {
            return null;
        }

        String key = resolveOAuth2AuthorizationRequestKey(request, state);
        logger.info("load: "+key);
        return authorizationRequests.get(key);
    }

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
        logger.info("remove: "+key);

        return authorizationRequests.remove(key);
    }


    private String resolveOAuth2AuthorizationRequestKey(HttpServletRequest request, String state) {
        UriComponents c = UriComponentsBuilder.fromPath(request.getRequestURI()).build();
        String lastSegment = c.getPathSegments().stream().reduce((first, second) -> second).orElse(null);
        return lastSegment+"/"+state;
    }




}