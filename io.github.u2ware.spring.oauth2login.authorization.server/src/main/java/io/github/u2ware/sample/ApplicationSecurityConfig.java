package io.github.u2ware.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

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
}
