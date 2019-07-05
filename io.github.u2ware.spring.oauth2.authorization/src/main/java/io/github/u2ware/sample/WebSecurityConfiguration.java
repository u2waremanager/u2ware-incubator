package io.github.u2ware.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Bean
	public PasswordEncoder passwordEncoder() throws Exception{
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new UserDetailsServices(passwordEncoder());
	}

	/////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/webjars/**","/resources/**");
//	}
    
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.and()
			.authorizeRequests()
				.mvcMatchers(JwkSetEndpoint.PATH).permitAll()
				.mvcMatchers(UserInfoEndpoint.PATH).permitAll()
				.mvcMatchers("/oauth/token").permitAll()
				.anyRequest().authenticated()
				.and()
				;
    }
}