package com.u2ware.sample.oauth2.authorization;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    protected static class SecurityConfig extends WebSecurityConfigurerAdapter {
//    	@Autowired
//    	private SecurityProperties securityProperties;
    //
//    	@Override
//        @Autowired // <-- This is crucial otherwise Spring Boot creates its own
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            logger.info("Defining inMemoryAuthentication (2 users)");
//            auth
//                    .inMemoryAuthentication()
    //
//                    .withUser(securityProperties.getUser().getName()).password("password")
//                    .roles("USER")
    //
//                    .and()
    //
//                    .withUser("admin").password("password")
//                    .roles("USER", "ADMIN")
//            ;
//        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .formLogin()
                    .and()
                    .httpBasic().disable()
                    .anonymous().disable()
                    .authorizeRequests().anyRequest().authenticated()
            ;
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    	protected Log logger = LogFactory.getLog(getClass());

    	private AuthenticationManager authenticationManager;

    	public AuthorizationServerConfig(AuthenticationConfiguration ac)throws Exception {
    		this.authenticationManager = ac.getAuthenticationManager();
    	}

    	@Autowired
    	protected OAuth2ClientProperties oauth2ClientProperties;

    	@Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.enabled}")
    	protected Boolean jwtEnabled;

    	@Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.privateKey}")
    	protected String jwtPrivateKey;

    	@Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.publicKey}")
    	protected String jwtPublicKey;

        

//    	@Bean
//    	public JwtAccessTokenConverter jwtAccessTokenConverter() {
//    		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    //
//    		//// keytool -genkeypair -alias hello -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass zaqwsx -keystore server.jks -storepass qweqwe
//    		Resource resource = new ClassPathResource("server.jks");
//    		char[] factoryPassword = "qweqwe".toCharArray();
    //
//    		String alias = "hello";
//    		char[] password = "zaqwsx".toCharArray();
    //
//    		KeyPair keyPair = new KeyStoreKeyFactory(resource, factoryPassword).getKeyPair(alias, password);
//    		converter.setKeyPair(keyPair);
    //
//    		return converter;
//    	}
    	@Bean
    	public JwtAccessTokenConverter jwtAccessTokenConverter() {
            logger.info("Initializing JWT with public key:\n" + jwtPublicKey);
    		
    		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    		converter.setSigningKey(jwtPrivateKey);
    		converter.setVerifierKey(jwtPublicKey);
    		return converter;
    	}

    	@Bean
    	public JwtTokenStore jwtTokenStore() {
    		return new JwtTokenStore(jwtAccessTokenConverter());
    	}		
    	

		@Override
    	@SuppressWarnings("deprecation")
    	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    		// AuthorizationServer 의 (자체) 보안 설정
    		security
    			.passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
	            .tokenKeyAccess("isAnonymous() || hasRole('ROLE_TRUSTED_CLIENT')") // permitAll()
	            .checkTokenAccess("hasRole('TRUSTED_CLIENT')"); // isAuthenticated()
    	}	

    	@Override
    	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    		// OAuth2 Client 정보 설정 
    		if(jwtEnabled) {
    			
    			oauth2ClientProperties.setClientId("trusted");
    			oauth2ClientProperties.setClientSecret("secret");

    			clients.inMemory()
    		        // Confidential client where client secret can be kept safe (e.g. server side)
    		        .withClient("confidential").secret("secret")
    		        .authorizedGrantTypes("client_credentials", "authorization_code", "refresh_token")
    		        .scopes("read", "write")
    		
    		        .and()
    		
    		        // Public client where client secret is vulnerable (e.g. mobile apps, browsers)
    		        .withClient("public") // No secret!
    		        .authorizedGrantTypes("implicit")
    		        .scopes("read")
    		
    		        .and()
    		
    		        // Trusted client: similar to confidential client but also allowed to handle user password
    		        .withClient("trusted").secret("secret")
    		        .authorities("ROLE_TRUSTED_CLIENT")
    		        .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
    		        .scopes("read", "write")
    		        .accessTokenValiditySeconds(60*60*24)
//    	            .autoApprove(true)
//    	            .redirectUris("http://localhost:8082/ui/login","http://localhost:8083/ui2/login","http://localhost:8082/login")
    	       ;
    	        
    		}else {

    			if (oauth2ClientProperties.getClientId() == null) {
    				oauth2ClientProperties.setClientId(UUID.randomUUID().toString());
    			}
    			clients.inMemory()
    				.withClient(oauth2ClientProperties.getClientId())
    				.secret(oauth2ClientProperties.getClientSecret())
    				.authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
    				.authorities("ROLE_USER").scopes("read", "write");
    		}
    		
    		String prefix = "security.oauth2.client";
    		logger.info(String.format(
    				"Initialized OAuth2 Client%n%n%s.client-id = %s%n%s.client-secret = %s%n%n",
    				prefix, oauth2ClientProperties.getClientId(), 
    				prefix, oauth2ClientProperties.getClientSecret()));
    	}
    	
    	@Override
    	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    		// AuthorizationServer 의 (작동을 위한) End-point 설정
    	    endpoints.authenticationManager(authenticationManager);
    		if(jwtEnabled) {
    			endpoints.tokenStore(jwtTokenStore());
    			endpoints.accessTokenConverter(jwtAccessTokenConverter());
    		}
    	}
    }
    
}