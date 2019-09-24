package com.example.demo.signin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import com.example.demo.core.SecurityUserRepository;

/**
 * @author u2waremanager@gmail.com
 */
@Service
public class SignInService implements UserDetailsService {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected @Autowired SecurityProperties securityProperties;
	protected @Autowired SecurityUserRepository securityUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDetails u = loadSystemUserByUsername(username);
		if(u!= null) return u;

		return securityUserRepository.findByUsername(username);
	}

	public UserDetails loadSystemUserByUsername(String username) {
		String systemUsername = securityProperties.getUser().getName();
		String systemPassword = securityProperties.getUser().getPassword();
		if(! systemUsername.equals(username)) 
			return null;
		
		UserDetails u = User.builder()
				.username(systemUsername)
				.password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(systemPassword))
				.roles("SECURITY")
				.build();
		logger.info(u);
		return u;
	}
}