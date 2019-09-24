package com.poscoict.rpa.controlroom.api.signin;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.poscoict.rpa.controlroom.api.ApplicationTests;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
public class AuthorizeRequestsTests extends ApplicationTests {

	public void contextLoads() throws Exception {

		UserDetails su = User.withUsername("su").password("su").authorities("ROLE_SUPER").build();
		UserDetails au = User.withUsername("au").password("au").authorities("ROLE_ADMIN").build();
		UserDetails u1 = User.withUsername("u1").password("u1").authorities("ROLE_USER").build();
		UserDetails u2 = User.withUsername("u2").password("u2").authorities("Oops").build();
	

		$.get("/index.html").is2xx();
		$.get("/index.html").U(su).is2xx();
		$.get("/index.html").U(au).is2xx();
		$.get("/index.html").U(u1).is2xx();
		$.get("/index.html").U(u2).is2xx();
		
		
		$.GET("/profile").is2xx();
		$.GET("/profile").U(su).is2xx();
		$.GET("/profile").U(au).is2xx();
		$.GET("/profile").U(u1).is2xx();
		$.GET("/profile").U(u2).is2xx();

		
		$.GET("/account").is4xx();
		$.GET("/account").U(su).is2xx();
		$.GET("/account").U(au).is2xx();
		$.GET("/account").U(u1).is2xx();
		$.GET("/account").U(u2).is4xx();
		
		
		$.GET("/organizations/!q").is4xx();
		$.GET("/organizations/!q").U(su).is2xx();
		$.GET("/organizations/!q").U(au).is4xx();
		$.GET("/organizations/!q").U(u1).is4xx();
		$.GET("/organizations/!q").U(u2).is4xx();
	}

}
