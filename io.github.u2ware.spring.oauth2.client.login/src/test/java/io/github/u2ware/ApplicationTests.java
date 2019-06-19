package io.github.u2ware;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	private static final String AUTHORIZATION_BASE_URI = "/login/oauth2/authorization";
	private static final String AUTHORIZE_BASE_URL = "http://localhost:8080/login/oauth2/code";

    private Log logger = LogFactory.getLog(getClass());

	@Autowired
	private WebClient webClient;

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Before
	public void setup() {
		this.webClient.getCookieManager().clearCookies();
	}

	@Test
	public void contextLoads() throws Exception{

        assertClientRegistrationRepository();
        
		ClientRegistration googleClientRegistration = this.clientRegistrationRepository.findByRegistrationId("google");
		ClientRegistration githubClientRegistration = this.clientRegistrationRepository.findByRegistrationId("github");
//		ClientRegistration facebookClientRegistration = this.clientRegistrationRepository.findByRegistrationId("facebook");
		ClientRegistration kakaoClientRegistration = this.clientRegistrationRepository.findByRegistrationId("kakao");
		ClientRegistration naverClientRegistration = this.clientRegistrationRepository.findByRegistrationId("naver");
        
		assertThat(googleClientRegistration).isNotNull();
		assertThat(githubClientRegistration).isNotNull();
//		assertThat(facebookClientRegistration).isNotNull();
		assertThat(kakaoClientRegistration).isNotNull();
		assertThat(naverClientRegistration).isNotNull();
        
        
        HtmlPage page = this.webClient.getPage("/");
        assertLoginPage(page);

        
        //
        assertWhenLinkClickedThenStatusRedirectForAuthorization(page, googleClientRegistration);
        assertWhenLinkClickedThenStatusRedirectForAuthorization(page, githubClientRegistration);
        assertWhenLinkClickedThenStatusRedirectForAuthorization(page, kakaoClientRegistration);
        assertWhenLinkClickedThenStatusRedirectForAuthorization(page, naverClientRegistration);
        
	}
	
	public void assertWhenLinkClickedThenStatusRedirectForAuthorization(HtmlPage page, ClientRegistration clientRegistration) throws Exception  {

		String registrationId = clientRegistration.getRegistrationId();
		String baseUri = AUTHORIZATION_BASE_URI + "/" + registrationId;
		String baseUrl = AUTHORIZE_BASE_URL + "/" + registrationId;
		String clientId = clientRegistration.getClientId();
		String scopes = clientRegistration.getScopes().stream().collect(Collectors.joining(" "));
		logger.info(registrationId);
		logger.info(clientId);
		logger.info(baseUri);
		logger.info(baseUrl);
		logger.info(scopes);

		List<HtmlAnchor> anchors = page.getAnchors();
		HtmlAnchor clientAnchorElement = this.getClientAnchorElement(anchors, baseUri);
		assertThat(clientAnchorElement).isNotNull();
		
		
		WebResponse response = this.followLinkDisableRedirects(clientAnchorElement);
		
		// statusCode
		int statusCode = response.getStatusCode();
		logger.info(statusCode);
		assertThat(statusCode).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
		
		// authorizeRedirectUri
		String authorizeRedirectUri = response.getResponseHeaderValue("Location");
		logger.info(authorizeRedirectUri);
		assertThat(authorizeRedirectUri).isNotNull();
		
		//
		UriComponents uriComponents = UriComponentsBuilder.fromUri(URI.create(authorizeRedirectUri)).build();
		Map<String, String> params = uriComponents.getQueryParams().toSingleValueMap();
		logger.info(uriComponents);
		logger.info(params);
		
		// requestUri
		String requestUri = uriComponents.getScheme() + "://" + uriComponents.getHost() + uriComponents.getPath();
		logger.info(requestUri);
		assertThat(requestUri).isEqualTo(clientRegistration.getProviderDetails().getAuthorizationUri());
		
		// params
		assertThat(params.get(OAuth2ParameterNames.RESPONSE_TYPE)).isEqualTo(OAuth2AuthorizationResponseType.CODE.getValue());
		assertThat(params.get(OAuth2ParameterNames.CLIENT_ID)).isEqualTo(clientId);
		assertThat(URLDecoder.decode(params.get(OAuth2ParameterNames.REDIRECT_URI), "UTF-8")).isEqualTo(baseUrl);
		assertThat(URLDecoder.decode(params.get(OAuth2ParameterNames.SCOPE), "UTF-8")).isEqualTo(scopes);
		assertThat(params.get(OAuth2ParameterNames.STATE)).isNotNull();
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////////////////////
	private HtmlAnchor getClientAnchorElement(List<HtmlAnchor> anchors, String href) throws Exception {
		Optional<HtmlAnchor> clientAnchorElement = anchors.stream()
				.filter(e -> e.getHrefAttribute().equals(href)).findFirst();
		return (clientAnchorElement.orElse(null));
	}
	
	private WebResponse followLinkDisableRedirects(HtmlAnchor anchorElement) throws Exception {
		WebResponse response = null;
		try {
			// Disable the automatic redirection (which will trigger
			// an exception) so that we can capture the response
			this.webClient.getOptions().setRedirectEnabled(false);
			anchorElement.click();
		} catch (FailingHttpStatusCodeException ex) {
			ex.printStackTrace();
			response = ex.getResponse();
			this.webClient.getOptions().setRedirectEnabled(true);
		}
		return response;
	}

	
	


	private void assertClientRegistrationRepository() throws Exception {
        logger.info(clientRegistrationRepository);
        logger.info(clientRegistrationRepository.getClass());
		
		ClientRegistration googleClientRegistration = this.clientRegistrationRepository.findByRegistrationId("google");
		ClientRegistration githubClientRegistration = this.clientRegistrationRepository.findByRegistrationId("github");
		ClientRegistration facebookClientRegistration = this.clientRegistrationRepository.findByRegistrationId("facebook");
		// ClientRegistration oktaClientRegistration = this.clientRegistrationRepository.findByRegistrationId("okta");
		ClientRegistration kakaoClientRegistration = this.clientRegistrationRepository.findByRegistrationId("kakao");
		ClientRegistration naverClientRegistration = this.clientRegistrationRepository.findByRegistrationId("naver");

        logger.info(googleClientRegistration.getClientName());
        logger.info(githubClientRegistration.getClientName());
        logger.info(facebookClientRegistration.getClientName());
        logger.info(kakaoClientRegistration.getClientName());
        logger.info(naverClientRegistration.getClientName());

        logger.info(googleClientRegistration.getRegistrationId());
        logger.info(githubClientRegistration.getRegistrationId());
        logger.info(facebookClientRegistration.getRegistrationId());
        logger.info(kakaoClientRegistration.getRegistrationId());
        logger.info(naverClientRegistration.getRegistrationId());

		String baseAuthorizeUri = AUTHORIZATION_BASE_URI + "/";
		String googleClientAuthorizeUri = baseAuthorizeUri + googleClientRegistration.getRegistrationId();
		String githubClientAuthorizeUri = baseAuthorizeUri + githubClientRegistration.getRegistrationId();
		String facebookClientAuthorizeUri = baseAuthorizeUri + facebookClientRegistration.getRegistrationId();
        // String oktaClientAuthorizeUri = baseAuthorizeUri + oktaClientRegistration.getRegistrationId();
		String kakaoClientAuthorizeUri = baseAuthorizeUri + kakaoClientRegistration.getRegistrationId();
		String naverClientAuthorizeUri = baseAuthorizeUri + naverClientRegistration.getRegistrationId();
        
        logger.info(googleClientAuthorizeUri);
        logger.info(githubClientAuthorizeUri);
        logger.info(facebookClientAuthorizeUri);
        logger.info(kakaoClientAuthorizeUri);
        logger.info(naverClientAuthorizeUri);
        
	}

	private void assertLoginPage(HtmlPage page) throws Exception {

		assertThat(page).isNotNull();
		
        String title = page.getTitleText();
		assertThat(title).isEqualTo("Oauth2 Login");

		List<HtmlAnchor> clientAnchorElements = page.getAnchors();
        for(HtmlAnchor a : clientAnchorElements){
            logger.info("a.getAttribute: "+a.getHrefAttribute());
            logger.info("a.asText: "+a.asText());
        }


		int expectedClients = 0;
        if(ClassUtils.isAssignableValue(InMemoryClientRegistrationRepository.class, clientRegistrationRepository)){
        	InMemoryClientRegistrationRepository r = (InMemoryClientRegistrationRepository)clientRegistrationRepository;
        	expectedClients = (int)StreamSupport.stream(r.spliterator(), false).count();
        }
		assertThat(clientAnchorElements.size()).isEqualTo(expectedClients);

		ClientRegistration googleClientRegistration = this.clientRegistrationRepository.findByRegistrationId("google");
		ClientRegistration githubClientRegistration = this.clientRegistrationRepository.findByRegistrationId("github");
		ClientRegistration facebookClientRegistration = this.clientRegistrationRepository.findByRegistrationId("facebook");
		// ClientRegistration oktaClientRegistration = this.clientRegistrationRepository.findByRegistrationId("okta");
		ClientRegistration kakaoClientRegistration = this.clientRegistrationRepository.findByRegistrationId("kakao");
		ClientRegistration naverClientRegistration = this.clientRegistrationRepository.findByRegistrationId("naver");

		String baseAuthorizeUri = AUTHORIZATION_BASE_URI + "/";
		String googleClientAuthorizeUri = baseAuthorizeUri + googleClientRegistration.getRegistrationId();
		String githubClientAuthorizeUri = baseAuthorizeUri + githubClientRegistration.getRegistrationId();
		String facebookClientAuthorizeUri = baseAuthorizeUri + facebookClientRegistration.getRegistrationId();
        // String oktaClientAuthorizeUri = baseAuthorizeUri + oktaClientRegistration.getRegistrationId();
		String kakaoClientAuthorizeUri = baseAuthorizeUri + kakaoClientRegistration.getRegistrationId();
		String naverClientAuthorizeUri = baseAuthorizeUri + naverClientRegistration.getRegistrationId();
        
		 for (int i=0; i<expectedClients; i++) {
		 	assertThat(
		 		clientAnchorElements.get(i).getHrefAttribute()
		 	).isIn(
		 		googleClientAuthorizeUri 
		 		,githubClientAuthorizeUri
		 		,facebookClientAuthorizeUri 
//		 		,oktaClientAuthorizeUri
		 		,kakaoClientAuthorizeUri 
		 		,naverClientAuthorizeUri 
		 	);
//		 	assertThat(
//		 		clientAnchorElements.get(2).asText()
//		 	).isIn(
//		 		googleClientRegistration.getClientName()
//		 		,githubClientRegistration.getClientName()
//		 		,facebookClientRegistration.getClientName()
////		 		,oktaClientRegistration.getClientName())
//		 		,kakaoClientRegistration.getClientName()
//		 		,naverClientRegistration.getClientName()
//		 	);
		 }
	}

}
