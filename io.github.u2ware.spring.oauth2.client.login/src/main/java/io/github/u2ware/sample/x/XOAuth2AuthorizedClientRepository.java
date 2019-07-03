package io.github.u2ware.sample.x;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.util.Assert;

//AuthenticatedPrincipalOAuth2AuthorizedClientRepository
public class XOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

    protected Log logger = LogFactory.getLog(getClass());

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private final OAuth2AuthorizedClientService authorizedClientService;
    private OAuth2AuthorizedClientRepository anonymousAuthorizedClientRepository = new HttpSessionOAuth2AuthorizedClientRepository();

    /**
     * Constructs a {@code AuthenticatedPrincipalOAuth2AuthorizedClientRepository} using the provided parameters.
     *
     * @param authorizedClientService the authorized client service
     */
    public XOAuth2AuthorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
        Assert.notNull(authorizedClientService, "authorizedClientService cannot be null");
        this.authorizedClientService = authorizedClientService;
    }

    /**
     * Sets the {@link OAuth2AuthorizedClientRepository} used for requests that are unauthenticated (or anonymous).
     * The default is {@link HttpSessionOAuth2AuthorizedClientRepository}.
     *
     * @param anonymousAuthorizedClientRepository the repository used for requests that are unauthenticated (or anonymous)
     */
    public final void setAnonymousAuthorizedClientRepository(OAuth2AuthorizedClientRepository anonymousAuthorizedClientRepository) {
        Assert.notNull(anonymousAuthorizedClientRepository, "anonymousAuthorizedClientRepository cannot be null");
        this.anonymousAuthorizedClientRepository = anonymousAuthorizedClientRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal,
                                                                        HttpServletRequest request) {
        if (this.isPrincipalAuthenticated(principal)) {
            logger.info("loadAuthorizedClient 1 ");                                                                    
            return this.authorizedClientService.loadAuthorizedClient(clientRegistrationId, principal.getName());
        } else {
            logger.info("loadAuthorizedClient 2 ");
            return this.anonymousAuthorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principal, request);
        }
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (this.isPrincipalAuthenticated(principal)) {
            logger.info("saveAuthorizedClient 1");
            this.authorizedClientService.saveAuthorizedClient(authorizedClient, principal);
        } else {
            logger.info("saveAuthorizedClient 2");                                                                    
            this.anonymousAuthorizedClientRepository.saveAuthorizedClient(authorizedClient, principal, request, response);
        }
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, Authentication principal,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (this.isPrincipalAuthenticated(principal)) {
            logger.info("removeAuthorizedClient 1");
            this.authorizedClientService.removeAuthorizedClient(clientRegistrationId, principal.getName());
        } else {
            logger.info("removeAuthorizedClient 2");
            this.anonymousAuthorizedClientRepository.removeAuthorizedClient(clientRegistrationId, principal, request, response);
        }
    }

    private boolean isPrincipalAuthenticated(Authentication authentication) {
        return authentication != null &&
                !this.authenticationTrustResolver.isAnonymous(authentication) &&
                authentication.isAuthenticated();
    }

}
