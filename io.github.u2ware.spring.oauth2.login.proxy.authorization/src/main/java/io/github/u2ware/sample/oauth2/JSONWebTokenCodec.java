package io.github.u2ware.sample.oauth2;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JSONWebTokenCodec {

	public static JSONWebTokenCodec withDefaultJWKSet() throws Exception {
		return new JSONWebTokenCodec();
	}
	public static JSONWebTokenCodec withJWKSet(JWKSet jwkSet) throws Exception {
		return new JSONWebTokenCodec(jwkSet);
	}
	public static JSONWebTokenCodec withJWKSetResource(Resource resource) throws Exception {
		return new JSONWebTokenCodec(resource);
	}
	
	protected Log logger = LogFactory.getLog(getClass());

	private JWKSet jwkSet;
	private JWK jwk;

	public JSONWebTokenCodec() throws Exception {
		this.jwkSet = JWKSet.load(new ClassPathResource("DefaultJWKSet.json", this.getClass().getClassLoader()).getInputStream());
		this.jwk = jwkSet.getKeys().get(0);
	}
	public JSONWebTokenCodec(JWKSet jwkSet) throws Exception {
		this.jwkSet = jwkSet;
		this.jwk = jwkSet.getKeys().get(0);
	}
	public JSONWebTokenCodec(Resource resource) throws Exception {
		this.jwkSet = JWKSet.load(resource.getInputStream());;
		this.jwk = jwkSet.getKeys().get(0);
	}

	
	public Map<String, Object> toJSONObject() {
		return jwkSet.toJSONObject(true);
	}

	
	public String encode(Jwt jwt) throws JOSEException {
        
		JWSHeader.Builder header = new JWSHeader.Builder(JWSAlgorithm.parse(jwk.getAlgorithm().getName())).keyID(jwk.toRSAKey().getKeyID());
		
        for(String key : jwt.getHeaders().keySet()) {
        	if(! CollectionUtils.contains(JWSHeader.getRegisteredParameterNames().iterator(), key)){
        		header.customParam(key, jwt.getHeaders().get(key));
        	}
        }

        
		JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
        for(String key : jwt.getClaims().keySet()) {
        	switch(key) {
		        case(JwtClaimNames.AUD) : {claimsSet.audience(jwt.getAudience()); break;}
		        case(JwtClaimNames.EXP) : {claimsSet.expirationTime(new Date(jwt.getExpiresAt().toEpochMilli()));  break;}
		        case(JwtClaimNames.IAT) : {claimsSet.issueTime(new Date(jwt.getIssuedAt().toEpochMilli())); break;}
		        case(JwtClaimNames.ISS) : {claimsSet.issuer(jwt.getClaimAsString(key));break;}
		        case(JwtClaimNames.JTI) : {claimsSet.jwtID(jwt.getClaimAsString(key));break;}
		        case(JwtClaimNames.NBF) : {claimsSet.notBeforeTime(new Date(jwt.getNotBefore().toEpochMilli())); break;}
		        case(JwtClaimNames.SUB) : {claimsSet.subject(jwt.getClaimAsString(key));break;}
		        default : {
		        	claimsSet.claim(key, jwt.getClaim(key));
		      		break;
		        }
        	}
        }
		return encode(new SignedJWT(header.build(), claimsSet.build()));
	}
	
	@SuppressWarnings("unchecked")
	public String encode(OAuth2AuthorizedClient client, OAuth2User user) throws JOSEException {
		
		JWSHeader.Builder header = new JWSHeader.Builder(JWSAlgorithm.parse(jwk.getAlgorithm().getName())).keyID(jwk.toRSAKey().getKeyID());
        
		JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();
        for(String key : user.getAttributes().keySet()) {
        	switch(key) {
		        case(JwtClaimNames.AUD) : {
		        	if(ClassUtils.isAssignableValue(List.class, user.getAttribute(key))){
			        	claimsSet.audience((List<String>)user.getAttribute(key)); 
		        	}else {
			        	claimsSet.audience(user.getAttribute(key).toString()); 
		        	}
		        	break;
		        }
		        case(JwtClaimNames.EXP) : { break;}
		        case(JwtClaimNames.IAT) : { break;}
		        case(JwtClaimNames.ISS) : {claimsSet.issuer(user.getAttribute(key).toString());break;}
		        case(JwtClaimNames.JTI) : {claimsSet.jwtID(user.getAttribute(key));break;}
		        case(JwtClaimNames.NBF) : { break;}
		        case(JwtClaimNames.SUB) : {claimsSet.subject(user.getAttribute(key));break;}
		        default : {
		        	claimsSet.claim(key, user.getAttribute(key));
		      		break;
		        }
        	}
        }
        
		if (client.getPrincipalName() != null) {
			claimsSet.subject(client.getPrincipalName());
			claimsSet.jwtID(client.getPrincipalName());
		}
		if (client.getAccessToken().getExpiresAt() != null) {
			claimsSet.expirationTime(new Date(client.getAccessToken().getExpiresAt().toEpochMilli()));
		}
		if (client.getAccessToken().getIssuedAt() != null) {
			claimsSet.issueTime(new Date(client.getAccessToken().getIssuedAt().toEpochMilli()));
		}
		claimsSet.claim("client_registration_id", client.getClientRegistration().getRegistrationId());
		claimsSet.claim("client_registration_name", client.getClientRegistration().getClientName());
		claimsSet.claim("authorities", new String[] {client.getClientRegistration().getRegistrationId().toUpperCase(),"USER"});
		
		
		return encode(new SignedJWT(header.build(), claimsSet.build()));
	}

	private String encode(SignedJWT signedJWT) throws JOSEException {
        signedJWT.sign(new RSASSASigner(jwk.toRSAKey()));
        String idToken = signedJWT.serialize();
		return idToken;
	}
	
	public Jwt decode(String idToken) throws JOSEException {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(jwk.toRSAKey().toRSAPublicKey()).build();
		return decoder.decode(idToken);
	}
	
	public Jwt decode(OAuth2AuthorizedClient client, OAuth2User user) throws JOSEException {
		
		Jwt.Builder token = Jwt.withTokenValue(client.getAccessToken().getTokenValue());
		token.header("alg", jwk.getAlgorithm().getName());
		token.header("kid", jwk.toRSAKey().getKeyID());
		
		
		if(user != null) {
			user.getAttributes().forEach((key,v)->{
	        	token.claim(key, user.getAttribute(key));
        	});
		}
		if (client.getPrincipalName() != null) {
			token.subject(client.getPrincipalName());
		}
		if (client.getAccessToken().getExpiresAt() != null) {
			token.expiresAt(client.getAccessToken().getExpiresAt());
		}
		if (client.getAccessToken().getIssuedAt() != null) {
			token.issuedAt(client.getAccessToken().getIssuedAt());
		}
		
		return token.build();
	}
}
