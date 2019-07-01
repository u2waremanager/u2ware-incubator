package io.github.u2ware.spring.oauth2.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.formLogin()
				.and()
			.authorizeRequests()
				.mvcMatchers("/oauth/info").permitAll()
                .mvcMatchers("/.well-known/jwks.json").permitAll()
				.anyRequest().authenticated()
				.and()
				;
		
    }
	
//	@Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//        	//.userDetailsService(userDetailsService())
//        	.passwordEncoder(passwordEncoder());
//    }
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
    
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(
				User
	//				.withDefaultPasswordEncoder()
	//				.username(USERNAME)
					.withUsername(USERNAME)
					.passwordEncoder((rawPassword)->{
						return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(rawPassword);
					})
					.password(PASSWORD)
					.roles(ROLES)
					.build());
	}
	
	
	public final static String USERNAME = "user";
	public final static String PASSWORD = "password";
	public final static String[] ROLES = new String[] {"USER"};
	
}
