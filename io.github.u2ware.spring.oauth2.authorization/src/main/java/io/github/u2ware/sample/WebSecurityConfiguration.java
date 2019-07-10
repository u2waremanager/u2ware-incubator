package io.github.u2ware.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public PasswordEncoder passwordEncoder() { 
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    };

    @Bean
    public RememberMeServices rememberMeServices() { 
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeServicesKey(), userDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    };

    public String rememberMeServicesKey() { 
        return getClass().getName();
    }
    
    
    @Autowired
    private UserDetailsService userDetailsService;
	/////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.formLogin()
				.and()
			.authorizeRequests()
				.mvcMatchers(KeyPairEndpoint.PATH).permitAll()
				.mvcMatchers(UserInfoEndpoint.PATH).permitAll()
				.anyRequest().authenticated()
                .and()
            .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(rememberMeServicesKey())
				;
    }
}