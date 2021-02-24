package io.github.u2ware.sample.secured;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService, InitializingBean{

	protected Log logger = LogFactory.getLog(getClass());
	
	public final static String USERNAME = "user";
	public final static String PASSWORD = "password";
	public final static String[] ROLES = new String[] {"USER"};
	
	
	private final Map<String, CustomUserDetails> users = new HashMap<>();
	private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername: "+username);
		return users.get(username);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
        
 		users.put(USERNAME, CustomUserDetails.builder()
 				.username(USERNAME)
 				.password(passwordEncoder.encode(PASSWORD))
 				.enabled(true)
 				.accountNonExpired(true)
 				.accountNonLocked(true)
 				.credentialsNonExpired(true)
 				.description("hello world~")
 				.build());
	}

	

}
