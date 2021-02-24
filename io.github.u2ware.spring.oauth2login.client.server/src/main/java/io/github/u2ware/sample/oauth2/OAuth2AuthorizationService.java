package io.github.u2ware.sample.oauth2;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.github.u2ware.sample.JKWSetService;

@Component
public class OAuth2AuthorizationService implements JKWSetService, InitializingBean{

	private JWKSet jwkSet;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.jwkSet = build();
	}

	@Override
	public JWKSet jwkSet() {
		return jwkSet;
	}
	
	
	@SuppressWarnings("unchecked")
	public String encode(OAuth2AuthorizedClient client, OAuth2User user) throws JOSEException {
		
		String algorithm = jwk().getAlgorithm().getName();
		String keyId = jwk().toRSAKey().getKeyID();
		JWSHeader.Builder header = new JWSHeader.Builder(JWSAlgorithm.parse(algorithm)).keyID(keyId);
        
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
	
	public Jwt decode(OAuth2AuthorizedClient client, OAuth2User user) throws JOSEException {
		
		String algorithm = jwk().getAlgorithm().getName();
		String keyId = jwk().toRSAKey().getKeyID();
		
		Jwt.Builder token = Jwt.withTokenValue(client.getAccessToken().getTokenValue());
		token.header("alg", algorithm);
		token.header("kid", keyId);
		
		
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
