package com.u2ware.elasticsearch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    protected ApplicationContext applicationContext;

	@Autowired
	protected MockMvc mvc;
	
	protected MockMvcWrapper $;
	
	@Before
	public void before() {
		$ = new MockMvcWrapper(mvc,"");
	}
	
	@Test
	public void contextLoads() throws Exception {
		
		logger.info(mvc);
		logger.info(mvc);
		logger.info(mvc);
		logger.info($);
		logger.info($);
		logger.info($);
//		logger.info(webClient);
//		logger.info(webClient);
//		logger.info(webClient);
		
		$.GET("/").is2xx();
		$.GET("/profile").is2xx();
		//mvc.perform(requestBuilder)
	}
}

