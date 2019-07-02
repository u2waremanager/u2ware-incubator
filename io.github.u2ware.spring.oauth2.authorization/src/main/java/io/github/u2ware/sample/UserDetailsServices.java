package io.github.u2ware.sample;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class UserDetailsServices implements UserDetailsService {

	public final static String USERNAME = "user";
	public final static String PASSWORD = "password";
	public final static String[] ROLES = new String[] {"USER"};

	private InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	public UserDetailsServices(PasswordEncoder encoder) {
		inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
			User.withUsername(USERNAME).password(encoder.encode(PASSWORD)).roles(ROLES).build()
		);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return inMemoryUserDetailsManager.loadUserByUsername(username);
	}
	
}
