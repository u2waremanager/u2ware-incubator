package io.github.u2ware.sample;

import java.util.HashMap;
import java.util.Map;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.u2ware.sample.x.NimbusEncoder;
import io.github.u2ware.sample.x.NimbusJwtDecoder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Oauth2Tests {

	
    private Log logger = LogFactory.getLog(getClass());

    //private @Autowired ApplicationContext applicationContext;
    

	@Test
	public void contextLoads() throws Exception{
		
		ClassPathResource JWKKeypairSet = new ClassPathResource("JWKKeypairSet.json");
		JWKSet jwkSet = JWKSet.load(JWKKeypairSet.getInputStream());
		JWK jwk = jwkSet.getKeyByKeyId("iscreammedia");
		
		JWSSigner signer = new RSASSASigner((RSAKey)jwk);
		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload("Hello, world!"));		
		jwsObject.sign(signer);
		String jwsToken = jwsObject.serialize();
		logger.info("JWS Token: "+jwsToken);
		
		
		NimbusEncoder encoder = new NimbusEncoder(jwkSet);
		NimbusJwtDecoder decoder = new NimbusJwtDecoder(JWKKeypairSet.getFile(), JwsAlgorithms.RS256);
        
        
        JWSHeader h = new JWSHeader(JWSAlgorithm.RS256);

		Map<String,Object> claims = new HashMap<>();	
		claims.put("hello", "world");
        
        
		String encodedClaims = encoder.claims(claims);
		logger.info("encodedClaims: "+encodedClaims);

        
		Map<String,Object> decodedClaims = decoder.claims(encodedClaims);
		logger.info("decodedClaims: "+decodedClaims);
		
        OAuth2AccessToken  d;
        //JwtAccessTokenConverter ds;
        //JwtHelper ds;
	}
}
