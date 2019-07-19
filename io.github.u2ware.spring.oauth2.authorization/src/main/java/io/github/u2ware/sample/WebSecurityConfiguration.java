package io.github.u2ware.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

    @Bean
    public PasswordEncoder userTokenPasswordEncoder() { 
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userTokenDetailsService() { 
        return new UserTokenManager(userTokenPasswordEncoder());
    }

    public AuthenticationProvider userTokenAuthenticationProvider() { 
        return new UserTokenAuthenticationProvider(userTokenPasswordEncoder());
    }

    public RememberMeServices userTokenRememberMeServices() { 
        // TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeServicesKey(), userDetailsService);
        XTokenBasedRememberMeServices rememberMeServices = new XTokenBasedRememberMeServices(userTokenRememberMeServicesKey(), userTokenDetailsService());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    };

    public String userTokenRememberMeServicesKey() { 
        return getClass().getName();
    }
   
	/////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.authenticationProvider(userTokenAuthenticationProvider())
        	.userDetailsService(userTokenDetailsService()).passwordEncoder(userTokenPasswordEncoder());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf()
				.disable()
			.cors()
				.and()
			.formLogin()
				.and()
			.authorizeRequests()
				.mvcMatchers(ResourceServerEndpoint.JWK_SET_URI).permitAll()
				.mvcMatchers(ResourceServerEndpoint.USER_INFO).permitAll()
				.anyRequest().authenticated()
                .and()
           .rememberMe()
               .rememberMeServices(userTokenRememberMeServices())
               .key(userTokenRememberMeServicesKey())
				;
    }
}