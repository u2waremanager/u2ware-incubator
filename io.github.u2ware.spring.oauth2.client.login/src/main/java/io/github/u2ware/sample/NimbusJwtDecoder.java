package io.github.u2ware.sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.RemoteKeySourceException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Resource;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

public class NimbusJwtDecoder {
    
    //NimbusJwtDecoderJwkSupport d;
    private Log logger = LogFactory.getLog(getClass());
    
	private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";

	private ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
    private Converter<Map<String, Object>, Map<String, Object>> claimSetConverter;
    private OAuth2TokenValidator<Jwt> jwtValidator;

	public NimbusJwtDecoder(File jwkSetFile) throws IOException, ParseException {
        this(jwkSetFile, JwsAlgorithms.RS256);
    }
	public NimbusJwtDecoder(URL jwkSetUrl) throws IOException, ParseException {
        this(jwkSetUrl, JwsAlgorithms.RS256);
    }
	public NimbusJwtDecoder(byte[] secret) throws IOException, ParseException {
        this(secret, JwsAlgorithms.RS256);
    }
	public NimbusJwtDecoder(JWKSet jwkSet) throws IOException, ParseException {
        this(jwkSet, JwsAlgorithms.RS256);
    }
	public NimbusJwtDecoder(File jwkSetFile, String jwsAlgorithm) throws IOException, ParseException {
        this(JWKSet.load(jwkSetFile), jwsAlgorithm);
    }
	public NimbusJwtDecoder(byte[] secret, String jwsAlgorithm) throws IOException, ParseException {
        this(new ImmutableSecret<>(secret), jwsAlgorithm);
    }
	@SuppressWarnings("rawtypes")
	public NimbusJwtDecoder(URL jwkSetUrl, String jwsAlgorithm) throws IOException, ParseException {
        this(new RemoteJWKSet(jwkSetUrl, new RestOperationsResourceRetriever()), jwsAlgorithm);
    }
	@SuppressWarnings("rawtypes")
	public NimbusJwtDecoder(JWKSet jwkSet, String jwsAlgorithm) throws IOException, ParseException {
        this(new ImmutableJWKSet(jwkSet), jwsAlgorithm);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public NimbusJwtDecoder(JWKSource jwkSource, String jwsAlgorithm) throws IOException, ParseException {
		Assert.notNull(jwkSource, "jwkSource cannot be empty");
		Assert.hasText(jwsAlgorithm, "jwsAlgorithm cannot be empty");

		JWSAlgorithm jwsAlg = JWSAlgorithm.parse(jwsAlgorithm);
		JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlg, jwkSource);
		this.jwtProcessor = new DefaultJWTProcessor<>();
		this.jwtProcessor.setJWSKeySelector(jwsKeySelector);
        this.jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {});
        
        this.jwtValidator = JwtValidators.createDefault();
        this.claimSetConverter = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
	}

	// public Map<String, Object> decodeCaims(String token) throws JwtException {
	// 	JWT jwt = this.parse(token);
	// 	try {
	// 		return createJwtClaims(jwt);
	// 	}catch(Exception ex) {
	// 		throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
	// 	}
	// }
	
	public Jwt decode(String token) throws JwtException {
		JWT jwt = this.parse(token);
		if (jwt instanceof SignedJWT) {
			Jwt createdJwt = this.createJwt(token, jwt);
			return this.validateJwt(createdJwt);
		}
		throw new JwtException("Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
	}

	private JWT parse(String token) {
		try {
			return JWTParser.parse(token);
		} catch (Exception ex) {
			throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
		}
	}
	
	private Map<String, Object> createJwtClaims(JWT parsedJwt) throws BadJOSEException, JOSEException{
		JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);
		Map<String, Object> claims = this.claimSetConverter.convert(jwtClaimsSet.getClaims());
		return claims;
	}
	

	private Jwt createJwt(String token, JWT parsedJwt) {
		Jwt jwt;

		try {
			// Verify the signature
			Map<String, Object> claims = createJwtClaims(parsedJwt);
			Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
			Instant expiresAt = (Instant) claims.get(JwtClaimNames.EXP);
			Instant issuedAt = (Instant) claims.get(JwtClaimNames.IAT);
			
			jwt = new Jwt(token, issuedAt, expiresAt, headers, claims);
		} catch (RemoteKeySourceException ex) {
			if (ex.getCause() instanceof ParseException) {
				throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Malformed Jwk set"));
			} else {
				throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
			}
		} catch (Exception ex) {
            ex.printStackTrace();
			if (ex.getCause() instanceof ParseException) {
				throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, "Malformed payload"));
			} else {
				throw new JwtException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
			}
		}

		return jwt;
	}

	private Jwt validateJwt(Jwt jwt){
		OAuth2TokenValidatorResult result = this.jwtValidator.validate(jwt);
		if (result.hasErrors()) {
			String description = result.getErrors().iterator().next().getDescription();
			throw new JwtValidationException(
					String.format(DECODING_ERROR_MESSAGE_TEMPLATE, description),
					result.getErrors());
		}

		return jwt;
	}

	private static class RestOperationsResourceRetriever implements ResourceRetriever {
		private RestOperations restOperations = new RestTemplate();

		@Override
		public Resource retrieveResource(URL url) throws IOException {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

			ResponseEntity<String> response;
			try {
				RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, url.toURI());
				response = this.restOperations.exchange(request, String.class);
			} catch (Exception ex) {
				throw new IOException(ex);
			}

			if (response.getStatusCodeValue() != 200) {
				throw new IOException(response.toString());
			}

			return new Resource(response.getBody(), "UTF-8");
		}
	}
}
