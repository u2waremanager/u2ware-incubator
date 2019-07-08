package com.example.demo.signin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author u2waremanager@gmail.com
 */
@Service
public class SignInService implements UserDetailsService {

	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //JdbcUserDetailsManager d;
        //User.builder().username(username).build();
        Principal userDetails = new Principal();
        return userDetails;
	}
}