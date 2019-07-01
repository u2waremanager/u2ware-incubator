package io.github.u2ware;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Oauth2Tests {

	
    private Log logger = LogFactory.getLog(getClass());

	private @Autowired WebClient webClient;
	private @Autowired MockMvc mockMvc;

	@Before
	public void setup() {
		this.webClient.getCookieManager().clearCookies();
		this.webClient.getOptions().setRedirectEnabled(true);
	}

	@Test
	public void contextLoads() throws Exception{
		
		
		HtmlPage page = this.webClient.getPage("/login/google");
		
        String title = page.getTitleText();
        logger.info(title);
        logger.info(page.getBaseURI());
        logger.info(page.getBaseURL());
        logger.info(page.getBody().asText());
	}
	
}
