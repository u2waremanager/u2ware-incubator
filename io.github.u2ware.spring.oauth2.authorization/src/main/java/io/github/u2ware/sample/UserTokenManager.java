package io.github.u2ware.sample;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserTokenManager implements UserDetailsService, InitializingBean{

	protected Log logger = LogFactory.getLog(getClass());
	
	public final static String USERNAME = "user";
	public final static String PASSWORD = "password";
	public final static String[] ROLES = new String[] {"USER"};
	
	
	private final Map<String, UserToken> users = new HashMap<>();
	private PasswordEncoder passwordEncoder;
	
	public UserTokenManager(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername: "+username);
		return users.get(username);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
        Map<String,Object> info = new HashMap<>();
        info.put("aaa", "bbb");
        
 		users.put(USERNAME, new UserToken(USERNAME, passwordEncoder.encode(PASSWORD), info));
	}
	
}
