package com.u2ware.sample.oauth2.resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	

    @Autowired
    private ApplicationContext context;
    
	@Autowired
	private MockMvc mockMvc;

	private MockMvcWrapper $;

	@Before
	public void setUp() {
		$ = new MockMvcWrapper(mockMvc,"");
	}
	
    
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    
    @Autowired
    private JwtAccessTokenHelper jwtAccessTokenHelper;

    @Test
	public void contextLoads() throws Exception {
		// Exception
		$.scan(context);
		
		$.scan(context, TokenStore.class);
		$.scan(context, JwtAccessTokenConverter.class);
		$.scan(context, ClientDetailsService.class);
		logger.info(jwtAccessTokenConverter);
		logger.info(jwtAccessTokenConverter.getClass());
		logger.info(jwtAccessTokenConverter.getKey());
		logger.info(jwtAccessTokenConverter.getAccessTokenConverter().getClass());

		
		////////////////////////////////////////
		//
		//////////////////////////////////////
//		String token1 = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTAwNzU2MDAsInVzZXJfbmFtZSI6InVzZXIiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiNjZmMmE3MTAtOWIyMy00YTcxLTkzZmYtNzViM2I0ODM1N2RjIiwiY2xpZW50X2lkIjoidHJ1c3RlZCIsInNjb3BlIjpbInJlYWQiXX0.Uo1ZnL0lAeFwD2VvhasmDW8PhaFJx_65tqQNoCXohlZZ0mXmyxaS9WPFX6U0lpMvZ083-ShFjKdWwbepcy7NrtQNtqNDGTes0aF0-00L9qkR3zdHS1SLdwmm-QJ9AnM7I-cPTX2hEaAl2JoD3sPS9bAmpK7pWCa-T7oigO-rnyBKG6gUnpX3B0wiNVmSMMs3je599jsH_x3Iyoxbm09uEHxLtEx5vjrFnbm2zoS7Qex98z0wkSbYvMZ-9NGp77MT6KBtoihhQXCAKqvXdVAgannIGkX5kkjuz-w-zY6AzeoKxbgkGAT7wEGD9w0DIKNfDPZoovHoXQPvduC8XygkpA";
//		$.GET("/").is4xx();
//		$.GET("/").H("Authorization", "Bearer " + token1).is2xx();

		//////////////////////////////////////////////////////
		//
		///////////////////////////////////////////////////////
		Map<String, Object> info1 = new HashMap<>();
		info1.put("scope", Arrays.asList("read"));
		info1.put("jti", UUID.randomUUID().toString());
		info1.put("exp", System.currentTimeMillis()/1000l + 60l);
		info1.put("user_name", "oops");
		info1.put("scope", Arrays.asList("read"));
		info1.put("authorities", Arrays.asList("ROLE_USER"));
		info1.put("client_id", "trusted");
		String token1 = jwtAccessTokenHelper.encode(info1);

		$.GET("/").is4xx();
		$.GET("/").H("Authorization", "Bearer " + token1).is2xx();
		
		//////////////////////////////////////////////////////
		//
		///////////////////////////////////////////////////////
		Map<String,Object> info2 = jwtAccessTokenHelper.decode(token1);
		String token2 = jwtAccessTokenHelper.encode(info2);

		$.GET("/").is4xx();
		$.GET("/").H("Authorization", "Bearer " + token2).is2xx();

		//////////////////////////////////////////////////////
		//
		///////////////////////////////////////////////////////
		logger.info(info1);
		logger.info(token1);
		
		logger.info(info2);
		logger.info(token2);
		Assert.assertEquals(token1, token2);
		
	}	
}