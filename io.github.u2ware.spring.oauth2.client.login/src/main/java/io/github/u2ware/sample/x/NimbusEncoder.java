package io.github.u2ware.sample.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;

public class NimbusEncoder {

	private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

	private final JWKSet jwkSet;
	private final JWSAlgorithm jwsAlgorithm;
	private final JWSSigner signer;

	
	public NimbusEncoder(File jwkSetFile) throws IOException, ParseException, JOSEException{
		this(JWKSet.load(jwkSetFile));
	}
	
	public NimbusEncoder(JWKSet jwkSet) throws IOException, ParseException, JOSEException{
		this.jwkSet = jwkSet;
		JWK jwk = jwkSet.getKeys().get(0);
		this.signer = new RSASSASigner((RSAKey)jwk);
		this.jwsAlgorithm = JWSAlgorithm.parse(jwk.getAlgorithm().getName());
	}
	
	public JWKSet getJWKSet(){
		return jwkSet;
	}
	
	public String claims(Map<String, Object> claims) throws JwtException {
		
		try {
			JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
			claims.forEach((k,v)->{claimsBuilder.claim(k, v);});
			
	    	Payload payload = new Payload(claimsBuilder.build().toJSONObject());
	    	JWSHeader header = new JWSHeader(jwsAlgorithm);

	    	
	      	JWSObject jwsObject = new JWSObject(header, payload);
	      	jwsObject.sign(signer);
	      	
			return jwsObject.serialize();
		} catch (Exception ex) {
			throw new JwtException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
		}
	}
	
	public String jwt(Jwt jwt) throws JwtException {
		return claims(jwt.getClaims());
	}
}
