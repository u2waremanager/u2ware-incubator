package com.example.demo.signin;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.ApplicationTests;
import com.example.demo.core.SecurityUser;
import com.example.demo.core.SecurityUserRepository;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
public class SignInTests extends ApplicationTests {

	protected @Autowired SecurityUserRepository securityUserRepository;
//	protected @Autowired SecurityAuthoritiesRepository securityAuthoritiesRepository;

	@Test
	public void contextLoads() throws Exception {
		
		SecurityUser user = new SecurityUser();
		user.setUsername("a");
		user.setPassword("a");
		user.setRoles("AAAAAA");
		
		user = securityUserRepository.save(user);
		
		logger.info(user);
		
		$.GET("/securityUsers/"+user.getId()).is2xx();
		

		$.post("/login").P("username", "hello").P("password", "world").is2xx();
		$.post("/login").P("username", "a").P("password", "a").is2xx();
		
		
//		PasswordEncoder p = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		logger.info(p.encode("aa"));
//		logger.info(p.matches("aa", p.encode("aa")));
//		
//		logger.info(p.encode("aa"));
//		logger.info(p.matches("aa", p.encode("aa")));
		
		//User not exists
//		$.post("/login").P(signin.loginForm("hello", "world")).is4xx();
//		$.get("/logout").is4xx();
//
//		
//		//login/ logout
//		ApplicationResultActions u = $.post("/login").P(signin.loginForm(userProperties)).is2xx().and(signin.loginDocs());
//		$.get("/logout").A(u).is2xx().and(signin.logoutDocs());
//		
//		
//		//Token Test
//		ApplicationResultActions u1 = $.post("/login").P(signin.loginForm(userProperties)).is2xx();
//		$.GET("/account").A(u1).is2xx();
//		$.GET("/account").A(u1).is4xx();
//		
//		ApplicationResultActions u2 = $.post("/login").P(signin.loginForm(userProperties)).is2xx();
//		u2 = $.GET("/account").A(u2).is2xx();
//		u2 = $.GET("/account").A(u2).is2xx();
	}
}
