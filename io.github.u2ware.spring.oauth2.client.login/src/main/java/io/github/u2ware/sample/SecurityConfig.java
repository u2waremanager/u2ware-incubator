package io.github.u2ware.sample;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf()
                .disable()
            
            // .cors()
            //     .configurationSource(corsConfigurationSource())
            //     .and()
            .authorizeRequests()
                // .anyRequest().permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/logout/*").permitAll()
                .antMatchers("/info").permitAll()
                .antMatchers("/info/*").permitAll()
                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .loginPage("/login") // 요청 URL 이 authenticated() 인 경우 "/login" 로 리다이렉트
                // .authorizedClientService(authorizedClientService) //InMemoryOAuth2AuthorizedClientService
                // .authorizedClientRepository(authorizedClientRepository) //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
                .authorizationEndpoint()
                    .authorizationRequestRepository(authorizationRequestRepository())
                    .baseUri("/oauth2/authorization/") // OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                    .and()
                .redirectionEndpoint()
                    .baseUri("/login/oauth2/code/*") // OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
                    .and() 
                // .tokenEndpoint()
                //     //.accessTokenResponseClient(accessTokenResponseClient)
                //     .and()
                .defaultSuccessUrl("/logon", true)
                    .permitAll()
                    .and()
            .logout()
                // .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login").permitAll()
                .and()
            ;
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new XOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
        return new XOAuth2AuthorizedClientRepository(authorizedClientService);
    }

    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new XAuthorizationRequestRepository();
    }

	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setMaxAge(86400l);
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "xsrf-token", "content-type", "content-Disposition", "content-transfer-encoding"));
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static class XOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService{

        protected Log logger = LogFactory.getLog(getClass());

        private final Map<String, OAuth2AuthorizedClient> authorizedClients = new ConcurrentHashMap<>();
        private final ClientRegistrationRepository clientRegistrationRepository;
    
        public Map<String, OAuth2AuthorizedClient> getAll(){
            return authorizedClients;
        }

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
            this.print("get", this.authorizedClients);
            return (T) this.authorizedClients.get(this.getIdentifier(registration, principalName));
        }
    
        @Override
        public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
            Assert.notNull(authorizedClient, "authorizedClient cannot be null");
            Assert.notNull(principal, "principal cannot be null");
            this.authorizedClients.put(this.getIdentifier(
                authorizedClient.getClientRegistration(), principal.getName()), authorizedClient);
            this.print("save", this.authorizedClients);
        }
    
        @Override
        public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
            Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
            Assert.hasText(principalName, "principalName cannot be empty");
            ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
            if (registration != null) {
                this.authorizedClients.remove(this.getIdentifier(registration, principalName));
                this.print("remove", this.authorizedClients);
            }
        }
    
        private String getIdentifier(ClientRegistration registration, String principalName) {
            String identifier = "[" + registration.getRegistrationId() + "][" + principalName + "]";
            return Base64.getEncoder().encodeToString(identifier.getBytes());
        }

        ////////////////////////////////////
        //
        /////////////////////////////////////
        private void print(String step,  Map<String, OAuth2AuthorizedClient> authorizedClients){
            authorizedClients.forEach((k, v)-> {
                logger.info(step+": "+v.getClientRegistration().getRegistrationId()+" "+v.getPrincipalName());
            });
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static class XOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository{

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
                this.print("loadAuthorizedClient 1 "+principal);                                                                    
                return this.authorizedClientService.loadAuthorizedClient(clientRegistrationId, principal.getName());
            } else {
                this.print("loadAuthorizedClient 2 "+principal);
                return this.anonymousAuthorizedClientRepository.loadAuthorizedClient(clientRegistrationId, principal, request);
            }
        }
    
        @Override
        public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal,
                                            HttpServletRequest request, HttpServletResponse response) {
            if (this.isPrincipalAuthenticated(principal)) {
                this.print("saveAuthorizedClient 1");
                this.authorizedClientService.saveAuthorizedClient(authorizedClient, principal);
            } else {
                this.print("saveAuthorizedClient 2");                                                                    
                this.anonymousAuthorizedClientRepository.saveAuthorizedClient(authorizedClient, principal, request, response);
            }
        }
    
        @Override
        public void removeAuthorizedClient(String clientRegistrationId, Authentication principal,
                                            HttpServletRequest request, HttpServletResponse response) {
            if (this.isPrincipalAuthenticated(principal)) {
                this.print("removeAuthorizedClient 1");
                this.authorizedClientService.removeAuthorizedClient(clientRegistrationId, principal.getName());
            } else {
                this.print("removeAuthorizedClient 2");
                this.anonymousAuthorizedClientRepository.removeAuthorizedClient(clientRegistrationId, principal, request, response);
            }
        }
    
        private boolean isPrincipalAuthenticated(Authentication authentication) {
            return authentication != null &&
                    !this.authenticationTrustResolver.isAnonymous(authentication) &&
                    authentication.isAuthenticated();
        }

        ////////////////////////////////////
        //
        /////////////////////////////////////
        private void print(String step){
            logger.info(step+": ");
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public static class XAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

        protected Log logger = LogFactory.getLog(getClass());

        private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME = XAuthorizationRequestRepository.class.getName() +  ".AUTHORIZATION_REQUEST";

        private final String sessionAttributeName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME;

        @Override
        public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
            Assert.notNull(request, "request cannot be null");
            String stateParameter = this.getStateParameter(request);
            if (stateParameter == null) {
                return null;
            }
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);
            this.print("load", authorizationRequests);
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
            request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);

            this.print("save", authorizationRequests);
        }

        @Override
        public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
            Assert.notNull(request, "request cannot be null");
            String stateParameter = this.getStateParameter(request);
            if (stateParameter == null) {
                return null;
            }
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = this.getAuthorizationRequests(request);

            OAuth2AuthorizationRequest originalRequest = authorizationRequests.remove(stateParameter);
            this.print("remove", authorizationRequests);
            if (!authorizationRequests.isEmpty()) {
                request.getSession().setAttribute(this.sessionAttributeName, authorizationRequests);
            } else {
                request.getSession().removeAttribute(this.sessionAttributeName);
            }
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
            Map<String, OAuth2AuthorizationRequest> authorizationRequests = session == null ? null :
                    (Map<String, OAuth2AuthorizationRequest>) session.getAttribute(this.sessionAttributeName);
            if (authorizationRequests == null) {
                return new HashMap<>();
            }
            this.print("get", authorizationRequests);
            return authorizationRequests;
        }


        ////////////////////////////////////
        //
        /////////////////////////////////////
        private void print(String step, Map<String, OAuth2AuthorizationRequest> authorizationRequests){
            authorizationRequests.forEach((k, v)-> {
                logger.info(step+": "+v.getAuthorizationUri()+" "+v.getState());
            });
        }
    }
}