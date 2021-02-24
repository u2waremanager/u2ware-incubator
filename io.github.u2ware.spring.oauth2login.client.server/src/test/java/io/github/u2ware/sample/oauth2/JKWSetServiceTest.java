package io.github.u2ware.sample.oauth2;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.github.u2ware.sample.JKWSetService;

public class JKWSetServiceTest {

	private Log logger = LogFactory.getLog(getClass());
	
//	@Test
	public void contextLoads1() throws Exception{
		
		String tokenValue = "abcdefg";
        Instant issuedAt = Instant.ofEpochSecond(1000);
        Instant expiresAt = Instant.now();
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();
        Jwt jwt = Jwt.withTokenValue(tokenValue)
        		.issuedAt(issuedAt)
        		.expiresAt(expiresAt)
        		.header("a", "a")
        		.subject("aaa")
        		.build();
        logger.info(jwt);
        logger.info(jwt.getAudience());

        
        
        
        JWKSet jwkSet = JWKSet.load( new ClassPathResource("JWKKeypairSet.json", this.getClass().getClassLoader()).getInputStream());
        logger.info(jwkSet);
        logger.info(jwkSet.getKeys());
        
        JWK jwk = jwkSet.getKeys().get(0);
        logger.info(jwk);
        logger.info(jwk.getAlgorithm());
        
        
//        RSAKey key = (RSAKey)jwk;
//        logger.info(key);

        JWSSigner singer = new RSASSASigner(jwk.toRSAKey());
        logger.info(singer);
        
        
        JWSHeader JWSHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
        		.keyID(jwk.toRSAKey().getKeyID())
        		.customParam("aa", "aa")
//        		.jwk(jwk)
        		.build();
        

        logger.info("++++"+JWSHeader.getIncludedParams());
        logger.info("++++"+JWSHeader.getIncludedParams());
        logger.info("++++"+JWSHeader.getIncludedParams());
        logger.info("++++"+JWSHeader.getCriticalParams());
        logger.info("++++"+JWSHeader.getCriticalParams());
        logger.info("++++"+JWSHeader.getCriticalParams());

        
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//        		.subject("alice")
//        		//.issuer("https://c2id.com")
//        		.build();
        
        JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder();
      for(String key : jwt.getClaims().keySet()) {
    	  logger.info(key+"==="+jwt.getClaim(key));
//    	  claimsSetBuilder = claimsSetBuilder.claim("sub", jwt.getClaim(key).toString());
    	  if("sub".equals(key))
    		  claimsSetBuilder = claimsSetBuilder.claim(key, jwt.getClaim(key));
//    	  claimsSetBuilder = claimsSetBuilder.subject("alice");
      }
      JWTClaimsSet claimsSet = claimsSetBuilder.build();

        
        
        
        SignedJWT signedJWT = new SignedJWT(JWSHeader, claimsSet);
        signedJWT.sign(singer);
        
        String idToken = signedJWT.serialize();
        logger.info(idToken);
        

        
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(jwk.toRSAKey().toRSAPublicKey()).build();
        Jwt jwt2 = decoder.decode(idToken);
        
        logger.info(jwt2);
        logger.info(jwt2.getHeaders());
        logger.info(jwt2.getClaims());
        logger.info(jwt2.getSubject());
        logger.info(jwt2.getSubject());
		
	}
	
	
	
//	private @Autowired  oauth2JwtService;
	
	@Test
	public void contextLoads2() throws Exception{
		
		String tokenValue = "abcdefg";
        Instant issuedAt = Instant.ofEpochSecond(1000);
        Instant expiresAt = Instant.now();
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> claims = new HashMap<>();
        claims.put("a", "b");
        claims.put("c", "d");
        
        
        Jwt jwt = Jwt.withTokenValue(tokenValue)
        		.issuedAt(issuedAt)
        		.expiresAt(expiresAt)
        		.header("a", "a")
        		.header("c", "d")
//        		.headers(null)
        		.subject("aaa")
        		.audience(StringUtils.commaDelimitedListToSet("a,b,c,d"))
        		.issuer("xx")
        		.jti("jjttii")
        		.claim("abcd", "abcd")
        		.claim("zzzz", claims)
        		.build();
        logger.info(jwt);
        logger.info(jwt.getExpiresAt());
		
        
        
        JKWSetService codec = new JKWSetService() {
        	
        	private JWKSet JWKSet;
        	
			@Override
			public JWKSet jwkSet() {
				if(JWKSet == null) JWKSet = build(true);
				return JWKSet;
			}
        };
        
        String idToken = codec.encode(jwt);
        logger.info(idToken);
		
        Jwt jwt2 = codec.decode(idToken);
        logger.info(jwt2);
        logger.info(jwt2.getHeaders());
        logger.info(jwt2.getClaims());
	}

}