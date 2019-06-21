package io.github.u2ware.sample;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .antMatchers("/login/*").permitAll()
            .antMatchers("/logout/*").permitAll()
            .antMatchers("/info/*").permitAll()
            .anyRequest().authenticated()
        .and()
        .oauth2Login()
            .loginPage("/login") //요청 URL 이 authenticated() 인 경우 "/" 로 리다이렉트 
            // .authorizationEndpoint().baseUri("/oauth2/authorization/").and() // OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
            // .redirectionEndpoint().baseUri("/login/oauth2/code/*").and()     // OAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
            .defaultSuccessUrl("/logon", true)
            .permitAll()
        .and()
        .logout()
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/login")
            .permitAll()
        .and()
        ;
    }
}