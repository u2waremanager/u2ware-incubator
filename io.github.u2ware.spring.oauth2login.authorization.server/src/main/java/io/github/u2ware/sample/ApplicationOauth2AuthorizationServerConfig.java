package io.github.u2ware.sample;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableWebSecurity
public class ApplicationOauth2AuthorizationServerConfig extends WebSecurityConfigurerAdapter {

	public final static String JWT_JWKS_JSON = "/.well-known/jwks.json";
	public final static String JWT_USER_INFO = "/user/info";

    public @Autowired UserDetailsService userDetailsService;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.userDetailsService(userDetailsService)
//        	.passwordEncoder(passwordEncoder())
        	;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeRequests(authorize -> authorize
			.antMatchers(JWT_JWKS_JSON).permitAll()
			.antMatchers(JWT_USER_INFO).permitAll()
		    .anyRequest().authenticated()
		    
		).formLogin();
    }
    
    @Bean
    Oauth2AuthorizationServerConfig oauth2AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return new Oauth2AuthorizationServerConfig(authenticationConfiguration);
    }
    
    
    @Configuration
    @EnableAuthorizationServer
    @SuppressWarnings("deprecation")
    public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

    	public final String CLIENT_ID = "Robbins";
    	public final String CLIENT_SECRET = "Baskin";
    	public final String[] SCOPES = new String[] {"READ", "WRITE"};
    	public final String[] GRANT_TYPES = new String[] {"password", "authorization_code"};
    	//public final static String[] GRANT_TYPES = new String[] {"password", "authorization_code", "refresh_token","client_credentials"};
//        public final static String[] REDIRECT_URIS = new String[] {
//        		"http://localhost:9091/login/oauth2/code/iScreamMediaLocal",
//        		"http://localhost:9091/login/oauth2/code/iScreamMedia"
//        };
    	
    	protected Log logger = LogFactory.getLog(getClass());

    	private @Autowired UserDetailsService userDetailsService;
    	private @Autowired UserDetailsClaimService userClaimsService;
    	private @Autowired JKWSetService jwkSetService;
    	
    	private AuthenticationManager authenticationManager;

    	public Oauth2AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    	}
    	
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
            	.allowFormAuthenticationForClients() // BASIC vs FORM
        		.tokenKeyAccess("permitAll()")
        		.checkTokenAccess("isAuthenticated()");
            ;
            
        }
    	

    	@Override
    	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    		
    		//clients.
    		//clients.jdbc(dataSource)
    		// @formatter:off
    		clients.inMemory()
    			.withClient(CLIENT_ID)
    				.authorizedGrantTypes(GRANT_TYPES)
    				.secret("{noop}"+CLIENT_SECRET)
    				.scopes("message:read")
                    .accessTokenValiditySeconds(600_000_000)
                    //.redirectUris(REDIRECT_URIS)
                    .autoApprove(true)
    				.and()
    		;
    		// @formatter:on
    	}
    	

    	
    	@Override
    	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    		

    		SimpleUserAuthenticationConverter userAuthenticationConverter = new SimpleUserAuthenticationConverter() ;
    		userAuthenticationConverter.setUserDetailsService(userDetailsService);
    		userAuthenticationConverter.setUserClaimsService(userClaimsService);
    		
    		
    		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
    		accessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
    		
    		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    		jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
    		jwtAccessTokenConverter.setKeyPair(jwkSetService.keyPair());
    		
    		// @formatter:off
    		endpoints
                .authenticationManager(this.authenticationManager)
    			.accessTokenConverter(jwtAccessTokenConverter)
                .tokenStore(new JwtTokenStore(jwtAccessTokenConverter))
                .redirectResolver((requestedRedirect, client)->{ return requestedRedirect;});
            ;
    		// @formatter:on
        }
    }    
    
	static class SimpleUserAuthenticationConverter extends DefaultUserAuthenticationConverter{
		
		private UserDetailsClaimService userClaimsService;
		
		public UserDetailsClaimService getUserClaimsService() {
			return userClaimsService;
		}

		public void setUserClaimsService(UserDetailsClaimService userClaimsService) {
			this.userClaimsService = userClaimsService;
		}

		@Override
		public Map<String, ?> convertUserAuthentication(Authentication authentication) {

			if(userClaimsService == null) {
				return super.convertUserAuthentication(authentication);
			}
			Map<String, Object> response = userClaimsService.convertUserDetails((UserDetails)authentication.getPrincipal());
			response.putAll(super.convertUserAuthentication(authentication));
			return response;
		}
	}
}
