package io.github.u2ware.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	public final static String AUTHORIZATION_ENDPOINT_BASE_URI = "/oauth2/authorization"; //OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;
	public final static String REDIRECTION_ENDPOINT_BASE_URI = "/login/oauth2/code/*"; //OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI
	
	
	public final static String OAUTH2_URI        = "/oauth2";
	public final static String OAUTH2_LOGIN_URI  = "/oauth2/login";
	public final static String OAUTH2_LOGON_URI  = "/oauth2/logon";
	public final static String OAUTH2_LOGOUT_URI = "/oauth2/logout";
	public final static String OAUTH2_LOGOFF_URI = "/oauth2/logff";

	
	public final static String JWT_JWKS_JSON = "/.well-known/jwks.json";
	public final static String JWT_USER_INFO = "/user/info";
	
	
	public final static String LOGIN_PROVIDER_PARAM = "provider";
	public final static String LOGIN_CALLBACK_PARAM = "callback";

	
	@Autowired
	private AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http
		.authorizeRequests(authorize -> authorize
			.antMatchers(JWT_JWKS_JSON).permitAll()
			.antMatchers(JWT_USER_INFO+"/**").permitAll()
			.antMatchers(OAUTH2_URI+"/**").permitAll()
		    .anyRequest().authenticated()
		)
        .oauth2Login(oauth2->oauth2
			.authorizationEndpoint(authorization -> authorization
				.baseUri(AUTHORIZATION_ENDPOINT_BASE_URI) 
				.authorizationRequestRepository(authorizationRequestRepository)
			)
			.redirectionEndpoint(redirection -> redirection
				.baseUri(REDIRECTION_ENDPOINT_BASE_URI) 
			)
			.defaultSuccessUrl(OAUTH2_LOGON_URI)
        )
        .logout(logout-> logout
        	.logoutSuccessUrl(OAUTH2_LOGOFF_URI)
        );
    }
}
 