package io.github.u2ware.sample;

import java.io.File;
import java.util.Date;

import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import com.nimbusds.jose.JOSEObjectType;
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

public class NimbusJwtEncoder {

    //private Log logger = LogFactory.getLog(getClass());

    private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";

    private final JWSAlgorithm jwsAlgorithm;
    private final JWSSigner signer;

    public NimbusJwtEncoder(File jwkSetFile) throws Exception {
        this(jwkSetFile, JwsAlgorithms.RS256);
    }
    public NimbusJwtEncoder(JWKSet jwkSet) throws Exception {
        this(jwkSet, JwsAlgorithms.RS256);
    }
    public NimbusJwtEncoder(JWK jwk) throws Exception {
        this(jwk, JwsAlgorithms.RS256);
    }
    public NimbusJwtEncoder(RSAKey key) throws Exception {
        this(key, JwsAlgorithms.RS256);
    }

    public NimbusJwtEncoder(JWSSigner signer) throws Exception {
        this(signer, JwsAlgorithms.RS256);
    }



    public NimbusJwtEncoder(File jwkSetFile, String jwsAlgorithm) throws Exception {
        this( JWKSet.load(jwkSetFile), jwsAlgorithm);
    }


	public NimbusJwtEncoder(JWKSet jwkSet, String jwsAlgorithm) throws Exception {
        this(jwkSet.getKeys().get(0), jwsAlgorithm);
    }

	public NimbusJwtEncoder(JWK jwk, String jwsAlgorithm) throws Exception {
        this((RSAKey)jwk, jwsAlgorithm);
    }

    public NimbusJwtEncoder(RSAKey key, String jwsAlgorithm) throws Exception {
        this( new RSASSASigner(key), jwsAlgorithm);
    }

    public NimbusJwtEncoder(JWSSigner signer, String jwsAlgorithm) throws Exception {
		this.signer = signer;
		this.jwsAlgorithm = JWSAlgorithm.parse(jwsAlgorithm);
	}

    
	public String encode(Jwt jwt) throws JwtException {
		try {
            JWSHeader header = new JWSHeader(jwsAlgorithm, JOSEObjectType.JWT, null, null,null,null,null,null,null,null,null,null,null);

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
			jwt.getClaims().forEach((k,v)->{claimsBuilder.claim(k, v);});
            claimsBuilder.expirationTime(Date.from(jwt.getExpiresAt()));
            claimsBuilder.issueTime(Date.from(jwt.getIssuedAt()));
            claimsBuilder.jwtID(jwt.getId());
            claimsBuilder.subject(jwt.getSubject());
            claimsBuilder.audience(jwt.getAudience());
            if(jwt.getNotBefore() != null) claimsBuilder.notBeforeTime(Date.from(jwt.getNotBefore()));
            if(jwt.getIssuer() != null) claimsBuilder.issuer(jwt.getIssuer().toString());

            Payload payload = new Payload(claimsBuilder.build().toJSONObject());
            JWSObject jwsObject = new JWSObject(header, payload);
	      	jwsObject.sign(signer);
	      	
			return jwsObject.serialize();
		} catch (Exception ex) {
            ex.printStackTrace();
			throw new JwtException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
		}
	}
}
