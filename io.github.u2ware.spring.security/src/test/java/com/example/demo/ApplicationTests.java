package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected @Value("${spring.data.rest.base-path:}") String springDataRestBasePath;
	protected @Value("${security.user.username:}") String freepassUsername;
	protected @Value("${security.user.password:}") String freepassPassword;
	protected @Value("${security.user.roles:}") String[] freepassRoles;

	protected @Autowired ObjectMapper objectMapper;
	protected @Autowired WebApplicationContext context;
	protected ApplicationMockMvc $;


	
	@Before
	public void before() {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
		this.$ = new ApplicationMockMvc(mvc, springDataRestBasePath);
		logger.info("----------------------------------------------------------------------------");
	}

	@After
	public void after() {
		logger.info("----------------------------------------------------------------------------");
	}
	
}
