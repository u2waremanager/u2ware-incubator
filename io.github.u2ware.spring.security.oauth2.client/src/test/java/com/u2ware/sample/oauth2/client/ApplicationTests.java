package com.u2ware.sample.oauth2.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected @Autowired ApplicationContext applicationContext;
    protected @Autowired MockMvc mockMvc;
	protected MockMvcWrapper $;

	
	@Before
	public void before() {
		$ = new MockMvcWrapper(mockMvc,"");
	}	
	@Test
	public void contextLoads() {
		

		logger.info(applicationContext);
		logger.info(mockMvc);
		logger.info($);
		
		
//		$.scan(applicationContext);
		$.scan(applicationContext, OAuth2ProtectedResourceDetails.class);
		$.scan(applicationContext, OAuth2ClientContext.class);
		$.scan(applicationContext, OAuth2RestOperations.class);
		
		
		
	}

}

