package io.github.u2ware.sample;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NimbusTest {

	
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    protected MockMvc mockMvc;

    
	@Test
	public void contextLoads() throws Exception{
        ///////////////////////////////////////////
        RequestEntity<?> request = RequestEntity
            .post(new URI("http://localhost:9093/oauth/token"))
            .header("Authorization", "Basic aGVsbG86c2VjcmV0")
            .body(MultiValueMapBuilder.add("grant_type", "password").add("username", "user").add("password", "password").build()
        );

		RestTemplate oAuth2RestTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        oAuth2RestTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        OAuth2AccessTokenResponse tokenResponse = oAuth2RestTemplate.exchange(request, OAuth2AccessTokenResponse.class).getBody();

        OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
        OAuth2RefreshToken refreshToken = tokenResponse.getRefreshToken();
        Map<String, Object> parameters = tokenResponse.getAdditionalParameters();
        logger.info(accessToken);
        logger.info(refreshToken);
        logger.info(parameters);


        ///////////////////////////////////////////
        NimbusJwtDecoderJwkSupport nimbus = new NimbusJwtDecoderJwkSupport("http://localhost:9093/.well-known/jwks.json");
        Jwt jwt = nimbus.decode(accessToken.getTokenValue());

        logger.info(jwt);
        logger.info(objectMapper.writeValueAsString(jwt));

        ///////////////////////////////////////////
    }
}
