package io.github.u2ware.sample;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private Log logger = LogFactory.getLog(getClass());

    private @Autowired OAuth2AuthorizedClientService clientService;
	private @Autowired InMemoryClientRegistrationRepository clientRegistrationRepository;

	@GetMapping("/")
	public String home(Model model) {
        logger.info(clientRegistrationRepository);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            logger.info(clientRegistrationId);

            OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
            model.addAttribute("client", client);
            return "logon";

            
        } else {
            List<ClientRegistration> registrations = 
            StreamSupport.stream(clientRegistrationRepository.spliterator(), true)
            // .map(clientRegistration -> new Registration(clientRegistration))
            .collect(Collectors.toList());

            model.addAttribute("registrations", clientRegistrationRepository);
            return "login";
        }
        // PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        // map.from(null).to(null);
    }

    @GetMapping("/user")
    public @ResponseBody OAuth2User user(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }
}
