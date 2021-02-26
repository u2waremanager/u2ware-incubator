package io.github.u2ware.sample.userAccounts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import io.github.u2ware.sample.ApplicationTests.RestMockMvc;
import io.github.u2ware.sample.ApplicationTests.RestMockMvcNuilder;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class UserAccountTests {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected RestMockMvc $;

	@BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		this.$  = RestMockMvcNuilder.of(context).docs(restDocumentation).secure(true).build();
    }
	
	@Test 
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void securitySampleTest1() throws Exception{
		$.GET("/userAccounts").is2xx();
	}

	@Test 
	public void securitySampleTest2() throws Exception{
		$.GET("/userAccounts").is4xx();
	}
	
	@Test 
	public void contextLoads() throws Exception{
		
		UserAccountToken user1 = UserAccountTokenBuilder.username("user1").build();
		UserAccountToken user2 = UserAccountTokenBuilder.username("user2").build();
		UserAccountToken admin = UserAccountTokenBuilder.username("admin").roles("ROLE_ADMIN").build();
		
		$.POST("/userAccounts").U(user1).C("username", "user1").is2xx().andExpect("username", "user1").andReturn("e1");
		$.POST("/userAccounts").U(user2).C("username", "user2").is2xx().andExpect("username", "user2").andReturn("e2");
		
		
		$.PUT("{e1}").U(user1).C("username", "user1Update").is2xx().andExpect("username", "user1Update").andReturn();
		$.PUT("{e2}").U(user1).C("username", "user1Update").is4xx();

		$.PUT("{e1}").U(user2).C("username", "user2Update").is4xx();
		$.PUT("{e2}").U(user2).C("username", "user2Update").is2xx().andExpect("username", "user2Update").andReturn();
		
		
		$.PUT("{e1}").U(admin).C("username", "change").is2xx().andExpect("username", "change").andReturn();
		$.PUT("{e2}").U(admin).C("username", "change").is2xx().andExpect("username", "change").andReturn();
		

		$.DELETE("{e1}").U(user1).is4xx();
	
	
	}
}
