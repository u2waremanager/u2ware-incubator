package com.example;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
public class AuthorizationServerApplication {
  public static void main(String[] args) {
      SpringApplication.run(AuthorizationServerApplication.class, args);
  }

	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
  
  
  @Configuration
  @EnableOAuth2Client
  @Order(200)
  public static class SecurityConfig extends WebSecurityConfigurerAdapter{


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http
      .formLogin()
      .and()
      .httpBasic().disable()
      .anonymous().disable()
      .authorizeRequests().anyRequest().authenticated()
      .and()
        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      .and()
        .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);


      // // @formatter:off
      // http.antMatcher("/**").authorizeRequests()
      //   .antMatchers("/", "/login**", "/logon**", "/webjars/**").permitAll()
      //   .anyRequest().authenticated().and().exceptionHandling()
      //     .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")).and().logout()
      //     .logoutSuccessUrl("/").permitAll().and().csrf()
      //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
      //     .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
      // // @formatter:on
    }
    private Filter ssoFilter() {
      CompositeFilter filter = new CompositeFilter();
      List<Filter> filters = new ArrayList<>();
      filters.add(ssoFilter(facebook(), "/login/facebook"));
      filters.add(ssoFilter(github(), "/login/github"));
      filters.add(ssoFilter(u2ware(), "/login/u2ware"));
      filter.setFilters(filters);
      return filter;
    }
  
    private Filter ssoFilter(ClientResources client, String path) {
      OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
          path);
      OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
      filter.setRestTemplate(template);
      UserInfoTokenServices tokenServices = new UserInfoTokenServices(
          client.getResource().getUserInfoUri(), client.getClient().getClientId());
      tokenServices.setRestTemplate(template);
      filter.setTokenServices(tokenServices);
      return filter;
    }
  
    @Bean
    @ConfigurationProperties("github")
    public ClientResources github() {
      return new ClientResources();
    }
  
    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
      return new ClientResources();
    }
  
  
    @Bean
    @ConfigurationProperties("u2ware")
    public ClientResources u2ware() {
      return new ClientResources();
    }
  
  
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;
  
  
    public static class ClientResources {
  
      @NestedConfigurationProperty
      private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();
    
      @NestedConfigurationProperty
      private ResourceServerProperties resource = new ResourceServerProperties();
    
      public AuthorizationCodeResourceDetails getClient() {
        return client;
      }
    
      public ResourceServerProperties getResource() {
        return resource;
      }
    }
    }


  
  @Configuration
  @EnableAuthorizationServer
  public static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

      private AuthenticationManager authenticationManager;

      public AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
      }

      @Override
      public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // AuthorizationServer 의 (자체) 보안 설정
    		security
    			.passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
	            .tokenKeyAccess("isAnonymous() || hasRole('ROLE_TRUSTED_CLIENT')") // permitAll()
	            .checkTokenAccess("hasRole('TRUSTED_CLIENT')"); // isAuthenticated()
      }
  
      @Override
      public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // OAuth2 Client 정보 설정 . setClientId, setClientSecret
        // clients.inMemory()
        //     .withClient("client")
        //     .secret("{noop}secret")
        //     .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
        //     .scopes("all");
        clients.inMemory()
        // Confidential client where client secret can be kept safe (e.g. server side)
        .withClient("confidential").secret("secret")
        .authorizedGrantTypes("client_credentials", "authorization_code", "refresh_token")
        .scopes("read", "write")
        .redirectUris("http://localhost:8080/client/", "http://localhost:18081/", "http://localhost:18081/login/u2ware")

        .and()

        // Public client where client secret is vulnerable (e.g. mobile apps, browsers)
        .withClient("public") // No secret!
        .authorizedGrantTypes("implicit")
        .scopes("read")
        .redirectUris("http://localhost:8080/client/", "http://localhost:18081/", "http://localhost:18081/login/u2ware")

        .and()

        // Trusted client: similar to confidential client but also allowed to handle user password
        .withClient("trusted").secret("secret")
        .authorities("ROLE_TRUSTED_CLIENT")
        .authorizedGrantTypes("client_credentials", "password", "authorization_code", "refresh_token")
        .scopes("read", "write")
        .accessTokenValiditySeconds(60*60*24)
        .redirectUris("http://localhost:8080/client/", "http://localhost:18081/", "http://localhost:18081/login/u2ware")
//    	            .autoApprove(true)
//    	            .redirectUris("http://localhost:8082/ui/login","http://localhost:8083/ui2/login","http://localhost:8082/login")
     ;
      }
  
      @Override
      public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // AuthorizationServer 의 (작동을 위한) End-point 설정
        endpoints.authenticationManager(authenticationManager);
        // endpoints.tokenStore(jwtTokenStore());
        // endpoints.accessTokenConverter(jwtAccessTokenConverter());
    }

    	// @Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.privateKey}")
    	// protected String jwtPrivateKey;

    	// @Value("${com.u2ware.sample.oauth2.authorization.AuthorizationServerConfig.jwt.publicKey}")
    	// protected String jwtPublicKey;

    	// @Bean
    	// public JwtAccessTokenConverter jwtAccessTokenConverter() {
    	// 	JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    	// 	converter.setSigningKey(jwtPrivateKey);
    	// 	converter.setVerifierKey(jwtPublicKey);
    	// 	return converter;
    	// }

    	// @Bean
    	// public JwtTokenStore jwtTokenStore() {
    	// 	return new JwtTokenStore(jwtAccessTokenConverter());
    	// }		
       
  }
}
