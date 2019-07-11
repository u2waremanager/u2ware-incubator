package io.github.u2ware.sample;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf()
                .disable()
            // .cors()
            //     .configurationSource(corsConfigurationSource())
            //     .and()
            .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/logout/*").permitAll()
                .antMatchers("/.well-known/jwks.json").permitAll()
                .antMatchers("/token/jwks.json").permitAll()
                .antMatchers("/token/clientRegistrations").permitAll()

                // .antMatchers("/token/decode").permitAll()
                // .antMatchers("/token/encode").permitAll()



                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .loginPage("/login") // 요청 URL 이 authenticated() 인 경우 "/login" 로 리다이렉트
                // .authorizedClientService(authorizedClientService) //InMemoryOAuth2AuthorizedClientService
                // .authorizedClientRepository(authorizedClientRepository) //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
                .authorizationEndpoint()
                    .authorizationRequestRepository(authorizationRequestRepository()) //HttpSessionOAuth2AuthorizationRequestRepository
                    .baseUri("/oauth2/authorization/") // OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                    .and()
                .redirectionEndpoint()
                    .baseUri("/login/oauth2/code/*") // OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
                    .and() 
//                .tokenEndpoint()
//                     .accessTokenResponseClient(accessTokenResponseClient())
//                     .and()
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

    
//    @Bean 
//    public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
//        return new XOAuth2AuthorizedClientService(clientRegistrationRepository);  //InMemoryOAuth2AuthorizedClientService
//    }
//
//    
//    @Bean
//    public OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
//        return new XOAuth2AuthorizedClientRepository(authorizedClientService); //AuthenticatedPrincipalOAuth2AuthorizedClientRepository
//    }
//    
    @Bean
    public  AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new InMemoryOAuth2AuthorizationRequestRepository(); 
        //AuthorizationRequestRepository<OAuth2AuthorizationRequest>
        //HttpSessionOAuth2AuthorizationRequestRepository
    }
    
   
    // CommonOAuth2Provider
}