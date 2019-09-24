package com.example.demo;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	protected final Log logger = LogFactory.getLog(getClass());

//	private @Autowired PasswordEncoder passwordEncoder;
	private @Autowired UserDetailsService userDetailsService;
	private @Autowired(required = false) RememberMeServices rememberMeServices;
	private @Autowired(required = false) PersistentTokenRepository rememberMeRepository;
	
	private @Autowired AuthenticationSuccessHandler authenticationSuccessHandler;
	private @Autowired AuthenticationFailureHandler authenticationFailureHandler;
	private @Autowired LogoutSuccessHandler logoutSuccessHandler;
	private @Autowired AccessDeniedHandler  accessDeniedHandler;
	private @Autowired AuthenticationEntryPoint	 authenticationEntryPoint;
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("userDetailsService: "+userDetailsService.getClass());
		auth.userDetailsService(userDetailsService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }
    
    protected RememberMeServices getRememberMeServices(){
        if(! ObjectUtils.isEmpty(rememberMeServices)){
            return rememberMeServices;

        }else if(! ObjectUtils.isEmpty(rememberMeRepository)){
            return new PersistentTokenBasedRememberMeServices(
                    rememberMeRepository.getClass().getName(), 
                    userDetailsService, 
                    rememberMeRepository);
        }else{
            return null;//throw new RuntimeException("rememberMe not found.");
        }
    }


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.formLogin()
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)
				.permitAll()
				.and()
			.logout()
				.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("JSESSIONID")
				.permitAll()
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
				.and()
			.authorizeRequests()
//				.antMatchers("/profile/**").authenticated()
//				.antMatchers("/join/**").permitAll()
//				.antMatchers(authenticationHandler.getAllowedPaths()).permitAll()
//				.antMatchers(authenticationHandler.getAuthenticatedPaths()).authenticated()
				.anyRequest().permitAll()
				.and()
			//////////////////////////////////////////////////
			// For 3rd App..
			//////////////////////////////////////////////////
			.cors()
				.and()
//			.rememberMe()
//				.rememberMeServices(getRememberMeServices())
////				.key(rememberMeServices.getKey())
//				.and()
//			.sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
			;
		
		RememberMeServices rememberMeServices = getRememberMeServices();
		if(rememberMeServices!= null) {
			http.rememberMe()
				.rememberMeServices(rememberMeServices);
//				.key(rememberMeServices)
		}
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setMaxAge(86400l);
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "xsrf-token", "content-type", "content-Disposition", "content-transfer-encoding"));
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
