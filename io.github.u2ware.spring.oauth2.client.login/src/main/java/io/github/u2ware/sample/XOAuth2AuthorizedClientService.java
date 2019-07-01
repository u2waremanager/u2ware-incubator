package io.github.u2ware.sample;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

//InMemoryOAuth2AuthorizedClientService
public class XOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {
    
    protected Log logger = LogFactory.getLog(getClass());

    private final Map<String, OAuth2AuthorizedClient> authorizedClients = new ConcurrentHashMap<>();
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * Constructs an {@code InMemoryOAuth2AuthorizedClientService} using the provided parameters.
     *
     * @param clientRegistrationRepository the repository of client registrations
     */
    public XOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration == null) {
            return null;
        }
        logger.info("load: "+clientRegistrationId+" "+principalName);
        T authorizedClient = (T) this.authorizedClients.get(this.getIdentifier(registration, principalName));
        logger.info("load: "+authorizedClients.size());
        return authorizedClient;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(principal, "principal cannot be null");

        logger.info("save: "+authorizedClient.getClientRegistration().getRegistrationId()+" "+principal.getName());

        this.authorizedClients.put(this.getIdentifier(
            authorizedClient.getClientRegistration(), principal.getName()), authorizedClient);

        logger.info("save: "+authorizedClients.size());
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration != null) {

            logger.info("remove: "+clientRegistrationId+" "+principalName);
            this.authorizedClients.remove(this.getIdentifier(registration, principalName));
            logger.info("remove: "+authorizedClients.size());
        }
    }

    private String getIdentifier(ClientRegistration registration, String principalName) {
        String identifier = "[" + registration.getRegistrationId() + "][" + principalName + "]";
        return Base64.getEncoder().encodeToString(identifier.getBytes());
    }

}