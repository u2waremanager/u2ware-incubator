package io.github.u2ware.spring.oauth2.authorizationserver;

//import static org.springframework.security.test.

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import io.github.u2ware.spring.oauth2.authorizationserver.config.AuthorizationServerConfig;
import io.github.u2ware.spring.oauth2.authorizationserver.config.WebSecurityConfig;

@RunWith(SpringRunner.class)
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

        
        Arrays.stream(applicationContext.getBeanDefinitionNames()).sorted().forEach(n-> {
        	
        	//String type = 
        	String type = applicationContext.getType(n).getName();
        	if(type.startsWith("org.springframework.security")|| type.startsWith("io.github.u2ware")) {
            	logger.info(n+"="+type);
        	}
        });
		
		
		
		
//        logger.info(username);
//        logger.info(password);
//        logger.info(CLIENT_ID);
//        logger.info(CLIENT_SECRET);
//
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", WebSecurityConfig.USERNAME);
        params.add("password", WebSecurityConfig.PASSWORD);
//        params.add("client_id", AuthorizationServerConfig.CLIENT_ID); //foo
//        params.add("scope", "message:read"); //read
    
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(AuthorizationServerConfig.CLIENT_ID, AuthorizationServerConfig.CLIENT_SECRET)) //foo, bar
                .accept("application/json;charset=UTF-8")) //"application/json;charset=UTF-8"
                .andDo(print())
                // .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));//"application/json;charset=UTF-8"
    
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String accessToken = jsonParser.parseMap(resultString).get("access_token").toString();
        logger.info(accessToken);
        
        
        mockMvc.perform(get("/oauth/check_token")
        		.param("token", accessToken)
        ).andDo(print());
        

        mockMvc.perform(get("/oauth/info")
        		.header("Authorization", "Bearer "+accessToken)
        ).andDo(print());
	}
}
