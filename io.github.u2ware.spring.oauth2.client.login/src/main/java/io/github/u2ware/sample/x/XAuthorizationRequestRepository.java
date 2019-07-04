package io.github.u2ware.sample.x;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;

//HttpSessionOAuth2AuthorizationRequestRepository
public class XAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    protected Log logger = LogFactory.getLog(getClass());

    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = XAuthorizationRequestRepository.class.getName() +  ".AUTHORIZATION_REQUEST";

    private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;

    //private final String CALLBACK_PARAMETER_NAME = "callback";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        logger.info("loadAuthorizationRequest: "+request.hashCode());
        logger.info("loadAuthorizationRequest: "+request.getSession().hashCode());

        String stateParameter = this.getStateParameter(request);
        if (stateParameter == null) {
            return null;
        }
        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);

        logger.info("load: "+stateParameter);
        logger.info("load: "+authorizationRequests.get(stateParameter));
        
        return authorizationRequests.get(stateParameter);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
                                            HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");


        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
        authorizationRequests.put(state, authorizationRequest);

        HttpSession session = request.getSession();
        session.setAttribute(this.sessionAttributeName, authorizationRequests);

        logger.info("save: "+request.getRequestURL());
        logger.info("save: "+state);
        logger.info("save: "+session.isNew()+" "+session.hashCode());
        logger.info("save: "+authorizationRequest.getAuthorizationRequestUri());
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        String stateParameter = this.getStateParameter(request);
        logger.info("remove: "+request.getRequestURL());
        logger.info("remove: "+stateParameter);
        
        Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
        if (stateParameter == null) {
            return null;
        }


        OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
        HttpSession session = request.getSession();

        if (!authorizationRequests.isEmpty()) {
            session.setAttribute(this.sessionAttributeName, authorizationRequests);
        } else {
            session.removeAttribute(this.sessionAttributeName);
        }
        logger.info("remove: "+session.isNew()+" "+session.hashCode());
        logger.info("remove: "+originalRequest);
        return originalRequest;
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(response, "response cannot be null");
        return this.removeAuthorizationRequest(request);
    }

    /**
     * Gets the state parameter from the {@link HttpServletRequest}
     * @param request the request to use
     * @return the state parameter or null if not found
     */
    private String getStateParameter(HttpServletRequest request) {
        return request.getParameter(OAuth2ParameterNames.STATE);
    }
    
    /**
     * Gets a non-null and mutable map of {@link OAuth2AuthorizationRequest#getState()} to an {@link OAuth2AuthorizationRequest}
     * @param request
     * @return a non-null and mutable map of {@link OAuth2AuthorizationRequest#getState()} to an {@link OAuth2AuthorizationRequest}.
     */
    private Map<String, OAuth2AuthorizationRequest> getAuthorizationRequests(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        logger.info("getAuthorizationRequests: "+(session == null ? null : session.hashCode()));

        Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
                (Map<String, OAuth2AuthorizationRequest>) session.getAttribute(this.sessionAttributeName);
        if (authorizationRequests == null) {
            logger.info("getAuthorizationRequests: 0");
            return new HashMap<>();
        }

        logger.info("getAuthorizationRequests: "+authorizationRequests.size());
        return authorizationRequests;
    }

}
