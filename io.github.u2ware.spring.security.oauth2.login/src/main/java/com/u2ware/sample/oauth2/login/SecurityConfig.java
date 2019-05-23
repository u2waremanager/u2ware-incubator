package com.u2ware.sample.oauth2.login;


import java.net.URI;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .antMatchers("/", "/logout").authenticated()
            .anyRequest().permitAll()
        .and()
        .oauth2Login()
            .loginPage("/")
            .authorizationEndpoint().baseUri("/login/oauth2/authorization").and()  // OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
            .redirectionEndpoint().baseUri("/login/oauth2/code/*").and() // OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
            // .userInfoEndpoint().userService(new ExtendsDefaultOAuth2UserService()).and()
            .defaultSuccessUrl("/", true)
            .permitAll()
        .and()
        .logout()
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/")
            .permitAll()
        .and()
        ;

        
        //OAuth2LoginAuthenticationFilter  s;
        //OAuth2LoginAuthenticationProvider d;
        //ApplicationConversionService.getSharedInstance().;
    }

    private static class ExtendsDefaultOAuth2UserService extends DefaultOAuth2UserService{

        public ExtendsDefaultOAuth2UserService(){
            super.setRequestEntityConverter(new OAuth2UserRequestEntityConverter(){
                private Log logger = LogFactory.getLog(getClass());
                

                @Override
                public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
                    ClientRegistration clientRegistration = userRequest.getClientRegistration();
            
                    HttpMethod httpMethod = HttpMethod.GET;
                    if (AuthenticationMethod.FORM.equals(clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod())) {
                        httpMethod = HttpMethod.POST;
                    }
                    HttpHeaders headers = new HttpHeaders();
                    //headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                            .build()
                            .toUri();
            
                    RequestEntity<?> request;
                    if (HttpMethod.POST.equals(httpMethod)) {

                        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"));
                        logger.info("--------2------------------------");
                        logger.info("---------2-----------------------");
                        logger.info(httpMethod.name());
                        logger.info(headers.getContentType());
                        logger.info(headers);
                        logger.info(uri);

                        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
                        formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
                        request = new RequestEntity<>(formParameters, headers, httpMethod, uri);
                    } else {

                        headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
                        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"));
                        logger.info("--------------------------------");
                        logger.info("--------------------------------");
                        logger.info(httpMethod.name());
                        logger.info(headers.getContentType());
                        logger.info(headers);
                        logger.info(uri);
                        logger.info("--------------------------------");
                        logger.info("--------------------------------");
                        request = new RequestEntity<>(headers, httpMethod, uri);
                    }
            
                    return request;
                }
            

                public RequestEntity<?> convert2(OAuth2UserRequest userRequest) {
        
                    ClientRegistration clientRegistration = userRequest.getClientRegistration();
                    AuthenticationMethod method = clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod();
                    HttpMethod httpMethod = null;
        
                    String value = method.getValue();
                    int idx = value.indexOf('/');
                    if(idx < 0){
                        httpMethod = AuthenticationMethod.FORM.equals(method) ? HttpMethod.POST : HttpMethod.GET;
                    }else{
                        String token1 = value.substring(0, idx);
                        String token2 = value.substring(idx+1);
                        httpMethod = HttpMethod.resolve(token1);
                        method = new AuthenticationMethod(token2);
                    }

                    logger.info("--------------------------------");
                    logger.info("--------------------------------");
                    logger.info(httpMethod.name());
                    logger.info(method.getValue());
                    logger.info("--------------------------------");
                    logger.info("--------------------------------");
        
                    // HttpMethod httpMethod = HttpMethod.GET;
                    // if (AuthenticationMethod.FORM.equals(clientRegistration.getProviderDetails().getUserInfoEndpoint().getAuthenticationMethod())) {
                    //     httpMethod = HttpMethod.POST;
                    // }
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                            .build()
                            .toUri();
            
                    RequestEntity<?> request;
                    if (AuthenticationMethod.FORM.equals(method)) {
                    //if (HttpMethod.POST.equals(httpMethod)) {
                        headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());
                        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"));
                        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
                        formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue());
                        request = new RequestEntity<>(formParameters, headers, httpMethod, uri);
                    } else {
                        headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());


                        logger.info("--------------------------------");
                        logger.info("--------------------------------");
                        logger.info(headers);
                        logger.info(uri);
                        logger.info("--------------------------------");
                        logger.info("--------------------------------");
    
                        request = new RequestEntity<>(headers, httpMethod, uri);
                    }
        
                    return request;
                }
            });
        }
    }
}