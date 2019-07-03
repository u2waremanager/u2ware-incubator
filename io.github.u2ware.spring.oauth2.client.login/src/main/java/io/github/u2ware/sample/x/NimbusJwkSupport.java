package io.github.u2ware.sample.x;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.RemoteKeySourceException;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;


@Component
public class NimbusJwkSupport {

    // NimbusJwtDecoderJwkSupport f;
    
    
	private static final String ENCODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to encode the Jwt: %s";
	private static final String DECODING_ERROR_MESSAGE_TEMPLATE = "An error occurred while attempting to decode the Jwt: %s";
	
	private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
	private final Converter<Map<String, Object>, Map<String, Object>> claimSetConverter;
    private final OAuth2TokenValidator<Jwt> jwtValidator;
    

	private RSAEncrypter encrypter;
	private RSADecrypter decrypter;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NimbusJwkSupport() throws Exception{

		
		//AccessTokenConverter d;
		//DefaultAccessTokenConverter fd;
		
		
		
		
//		keytool -genkey -alias iScreamMedia -keyalg RSA -keystore keystore.jks
		
        String privateExponent = "3851612021791312596791631935569878540203393691253311342052463788814433805390794604753109719790052408607029530149004451377846406736413270923596916756321977922303381344613407820854322190592787335193581632323728135479679928871596911841005827348430783250026013354350760878678723915119966019947072651782000702927096735228356171563532131162414366310012554312756036441054404004920678199077822575051043273088621405687950081861819700809912238863867947415641838115425624808671834312114785499017269379478439158796130804789241476050832773822038351367878951389438751088021113551495469440016698505614123035099067172660197922333993";
        String modulus = "18044398961479537755088511127417480155072543594514852056908450877656126120801808993616738273349107491806340290040410660515399239279742407357192875363433659810851147557504389760192273458065587503508596714389889971758652047927503525007076910925306186421971180013159326306810174367375596043267660331677530921991343349336096643043840224352451615452251387611820750171352353189973315443889352557807329336576421211370350554195530374360110583327093711721857129170040527236951522127488980970085401773781530555922385755722534685479501240842392531455355164896023070459024737908929308707435474197069199421373363801477026083786683";
        String exponent = "65537";
        
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
        RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));

        KeyFactory factory = KeyFactory.getInstance("RSA");
        KeyPair keyPair = new KeyPair(factory.generatePublic(publicSpec), factory.generatePrivate(privateSpec));
        
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
		RSAPrivateKey privateKey =(RSAPrivateKey)keyPair.getPrivate();

		this.encrypter = new RSAEncrypter(publicKey);
		this.decrypter = new RSADecrypter(privateKey);
		
		JWSAlgorithm jwsAlgorithm = JWSAlgorithm.parse(JwsAlgorithms.RS256);
		JWKSource jwkSource = new ImmutableSecret<>(privateKey.getEncoded());
		JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgorithm, jwkSource);
		
		this.jwtProcessor = new DefaultJWTProcessor<>();
		this.jwtProcessor.setJWSKeySelector(jwsKeySelector);		
		this.jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {});
		
		this.claimSetConverter = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());
		this.jwtValidator = JwtValidators.createDefault();
		
	}

	public Map<String, Object> getPublicKey() {
        RSAKey key = new RSAKey.Builder(encrypter.getPublicKey()).build();
        return new JWKSet(key).toJSONObject();
    }
	
	public String encode(Jwt token) throws JwtException {

		try {
			JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder();
			token.getClaims().forEach((k,v)->{claimsBuilder.claim(k, v);});
	
	    	Payload payload = new Payload(claimsBuilder.build().toJSONObject());
	      	JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP, EncryptionMethod.A128GCM);
	      	
	      	JWEObject jweObject = new JWEObject(header, payload);
	      	jweObject.encrypt(this.encrypter);
			
			return jweObject.serialize();
		} catch (Exception ex) {
			throw new JwtException(String.format(ENCODING_ERROR_MESSAGE_TEMPLATE, ex.getMessage()), ex);
		}
	}
	
	
	public Jwt decode(String token) throws JwtException, ParseException {
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

	private Jwt createJwt(String token, JWT parsedJwt) {
		Jwt jwt;

		try {
			// Verify the signature
			JWTClaimsSet jwtClaimsSet = this.jwtProcessor.process(parsedJwt, null);

			Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
			Map<String, Object> claims = this.claimSetConverter.convert(jwtClaimsSet.getClaims());

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
			throw new JwtValidationException(String.format(DECODING_ERROR_MESSAGE_TEMPLATE, description), result.getErrors());
		}
		return jwt;
	}
	
}
