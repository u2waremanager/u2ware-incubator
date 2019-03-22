package com.example;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationServerApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected @Autowired ApplicationContext applicationContext;
    protected @Autowired MockMvc mvc;
	protected MockMvcWrapper $;

	
	@Before
	public void before() {
		$ = new MockMvcWrapper(mvc,"");
	}	

	@Test
	public void contextLoads() throws Exception {
		

	    //////////////////////////////////////
	    // Authorization Code Flow
	    //////////////////////////////////////
	    















	    //////////////////////////////////////
	    // Resource Owner Password Credentials Flow
		//////////////////////////////////////
		RequestPostProcessor w = httpBasic("user", "user");

		MultiValueMap<String, String> p1 = new LinkedMultiValueMap<>();
	    p1.add("grant_type", "password");
	    p1.add("username", "user");
	    //p1.add("password", "user");
		
	    $.POST("/oauth/token").P(p1).is2xx();


		// //http://localhost:8080/oauth/authorize?
	    // MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
	    // params1.add("client_id", "client");
	    // params1.add("grant_type", "authorization_code");
	    // params1.add("scope", "read");
	    // params1.add("response_type", "code");
	    // params1.add("redirect_uri", "http://naver.com");
	    

		//AuthorizationEndpoint.authorize(AuthorizationEndpoint.java:143)

		// this.mvc.perform(post("/oauth/token")
		// .param("grant_type", "password")
		// .param("username", "subject")
		// .param("password", "password")
		// .header("Authorization", "Basic cmVhZGVyOnNlY3JldA=="))
		// 	.andExpect(status().isOk());		
		
	}
}

