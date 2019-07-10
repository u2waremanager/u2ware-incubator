package io.github.u2ware.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServices implements UserDetailsService, InitializingBean{

	protected Log logger = LogFactory.getLog(getClass());
	
	public final static String USERNAME = "user";
	public final static String PASSWORD = "password";
	public final static String[] ROLES = new String[] {"USER"};

	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
    
    @Autowired
    private PasswordEncoder encoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername: "+username);
		return inMemoryUserDetailsManager.loadUserByUsername(username);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
			User.withUsername(USERNAME).password(encoder.encode(PASSWORD)).roles(ROLES).build()
		);
	}
}
