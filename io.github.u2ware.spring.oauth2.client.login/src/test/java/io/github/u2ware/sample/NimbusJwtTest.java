package io.github.u2ware.sample;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NimbusJwtTest {

	
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    protected MockMvc mockMvc;

    
	@Test
	public void contextLoads() throws Exception{


        ///////////////////////////////////////////
        // localhost:9093 ...
        ///////////////////////////////////////////
        RequestEntity<?> tokenRequest = RequestEntity
            .post(new URI("http://localhost:9093/oauth/token"))
            .header("Authorization", "Basic aGVsbG86c2VjcmV0")
            .body(MultiValueMapBuilder.add("grant_type", "password").add("username", "user").add("password", "password")
            .build()
        );
        logger.info("tokenRequest: "+tokenRequest);

		RestTemplate tokenTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        tokenTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        OAuth2AccessTokenResponse tokenResponse = tokenTemplate.exchange(tokenRequest, OAuth2AccessTokenResponse.class).getBody();
        logger.info("tokenResponse: "+tokenResponse);

        OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
        OAuth2RefreshToken refreshToken = tokenResponse.getRefreshToken();
        Map<String, Object> parameters = tokenResponse.getAdditionalParameters();
        logger.info("tokenResponse: "+accessToken);
        logger.info("tokenResponse: "+refreshToken);
        logger.info("tokenResponse: "+parameters);

        ///////////////////////////////////////////
        // Token 
        ///////////////////////////////////////////
        NimbusJwtDecoder nimbus = new NimbusJwtDecoder(new URL("http://localhost:9093/.well-known/jwks.json"));
        Jwt decodedJwt = nimbus.decode(accessToken.getTokenValue());

        logger.info(decodedJwt);
        logger.info(objectMapper.writeValueAsString(decodedJwt));

        ///////////////////////////////////////////
        // User info 
        ///////////////////////////////////////////
        RequestEntity<?> userRequest = RequestEntity
            .get(new URI("http://localhost:9093/user/info"))
            .header("Authorization", "Bearer "+accessToken.getTokenValue())
            .build();



		RestTemplate userTemplate = new RestTemplate();
        userTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        
        Map<String, Object> userResponse = userTemplate.exchange(userRequest, Map.class).getBody();
        logger.info(userResponse);



        ///////////////////////////////////////////
        //  Encode JWT
        ///////////////////////////////////////////
        File jwkSetFile = new ClassPathResource("JWKKeypairSet.json").getFile();


        String tokenValue = accessToken.getTokenValue();
        Instant issuedAt = accessToken.getIssuedAt();
        Instant expiresAt = accessToken.getExpiresAt();
        
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "RS256");
        Map<String, Object> claims = new HashMap<>(userResponse);

        if(! claims.containsKey(JwtClaimNames.JTI)){
            claims.put(JwtClaimNames.JTI, UUID.randomUUID());
        }
        Jwt jwt = new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);

        String jwtToken =  new NimbusJwtEncoder(jwkSetFile).encode(jwt);
        logger.info(jwt);
        logger.info(objectMapper.writeValueAsString(jwt));
        logger.info(jwtToken);


        ///////////////////////////////////////////
        //  Decode JWT 1
        ///////////////////////////////////////////
        Jwt jwt1 = new NimbusJwtDecoder(jwkSetFile).decode(jwtToken);
        logger.info(jwt1);
        logger.info(objectMapper.writeValueAsString(jwt1));


        Jwt jwt2 = new NimbusJwtDecoder(new URL("http://localhost:9091/token/jwks.json")).decode(jwtToken);
        logger.info(jwt2);
        logger.info(objectMapper.writeValueAsString(jwt2));
     
        
        ///////////////////////////////////////////
        //  Encode JWT2
        ///////////////////////////////////////////
        logger.info("---------------------------------------------");
        logger.info("---------------------------------------------");
        logger.info("---------------------------------------------");
        logger.info("---------------------------------------------");
        String token3 = 
"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiN2hHcXdpYVBrcF9CdUNZNEJ5UDlnZyIsInN1YiI6IjEwMzM1NzM5Mzk5NzAwMzQwMDM2MCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczpcL1wvYWNjb3VudHMuZ29vZ2xlLmNvbSIsImdpdmVuX25hbWUiOiJMZWUiLCJsb2NhbGUiOiJrbyIsInBpY3R1cmUiOiJodHRwczpcL1wvbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbVwvLU5idDZIZzhYSXhNXC9BQUFBQUFBQUFBSVwvQUFBQUFBQUFBTjhcL0c0bkw5ZDZVT0J3XC9zOTYtY1wvcGhvdG8uanBnIiwiYXVkIjoiOTU5Njg2NjE1Mzk2LWs1YWM5bjVnZnUxdXBxOTNmaDJubnNubmk3bWdjZW5uLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXpwIjoiOTU5Njg2NjE1Mzk2LWs1YWM5bjVnZnUxdXBxOTNmaDJubnNubmk3bWdjZW5uLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwibmFtZSI6IkxlZSBLd2FuZ1NpayIsImV4cCI6e30sImZhbWlseV9uYW1lIjoiS3dhbmdTaWsiLCJpYXQiOnt9LCJlbWFpbCI6InUyd2FyZW1hbmFnZXJAZ21haWwuY29tIn0.AVjZYPD8InYdNaWLykvwsMb-ZHTED060uByDpp8XtvPibAR0jGij50UM3rKz-ssZaZOEXsToHfSLkbr6uTOqqcqOkXOfBRF6Fqm0uPmPmrq1J8xkdsEHqAH9GldjhoKl2YM-H9I01T5Bj-Avp7nAvjnZTYsdSRU47YoKDrv9bNLQ00sefRI1iDkfWJ2ACZZgF-EZRMhAtj3mIr1p00_dJkPxQmBC2SqMNoBEQxh1l_LNHd3EN4I2ZaTCrVO6aeFzrdkQpOO7bTzpG1vjjlqnzX4cC_uIgU-cTCzvmmN0EtfsLEJrfWjysD4DD1DeXzbrbX6evREHZKXZy9obFnNjHA"
        ;
        Jwt jwt3 = new NimbusJwtDecoder(new URL("http://localhost:9091/token/jwks.json")).decode(token3);
        logger.info(jwt3);
        logger.info(objectMapper.writeValueAsString(jwt2));


        //DefaultJWTProcessor f;
    }
}
