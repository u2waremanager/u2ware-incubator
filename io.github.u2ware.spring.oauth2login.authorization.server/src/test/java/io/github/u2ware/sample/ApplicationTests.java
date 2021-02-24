package io.github.u2ware.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.nimbusds.jose.jwk.source.JWKSource;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private WebApplicationContext applicationContext;
	
	@Autowired
    protected MockMvc mockMvc;
    
	@Test
    public void contextLoads() throws Exception {

		
		
		
		
	}
}
