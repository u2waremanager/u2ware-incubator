package io.github.u2ware.spring.websocket;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestBaseTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	public final @Rule JUnitRestDocumentation restDocumentation  = new JUnitRestDocumentation();
	public final String restUrisSchema = "http";
	public final String restUrisHost = "localhost";
	public final Integer restUrisPort = 8080;
	
	public @Value("${spring.data.rest.base-path:}") String springDataRestBasePath;
	public @Autowired WebApplicationContext context;
	public RestMockMvc $;
	
	@Before
	public void before() {
		MockMvc mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(documentationConfiguration(restDocumentation)
					.uris().withScheme(restUrisSchema).withHost(restUrisHost).withPort(restUrisPort)
				).build();
		this.$ = new RestMockMvc(mvc, springDataRestBasePath);
		logger.info("----------------------------------------------------------------------------");
	}
	
	@Test
	public void contextLoads() {
		logger.info("----------------------------------------------------------------------------");
	}
}
