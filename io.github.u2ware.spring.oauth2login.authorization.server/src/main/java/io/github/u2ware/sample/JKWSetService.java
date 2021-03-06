package io.github.u2ware.sample;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.util.Map;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;

public interface JKWSetService {

	public JWKSet jwkSet();
	
	default Map<String, Object> jwks() {
		return jwkSet().toJSONObject();
	}
	
	default JWK jwk() {
		return jwkSet().getKeys().get(0);
	}
	
	default KeyPair keyPair() {
		try {
	        RSAKey key = jwk().toRSAKey();
			PublicKey publicKey = key.toPublicKey();
	        PrivateKey privateKey = key.toPrivateKey();
			return new KeyPair(publicKey, privateKey);
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}
	
	default Jwt decode(String idToken) throws JOSEException {
		RSAPublicKey key = jwk().toRSAKey().toRSAPublicKey();
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(key).build();
		return decoder.decode(idToken);
	}
	
	default JWKSet build() {
		return build(false);
	}
	
	default JWKSet build(boolean generate) {
		if(generate) {
			return buildWith(KeyGeneratorUtils.generateRsa());
		}else {
			return buildWith(new ClassPathResource("JWKSetBuilder.json"));
		}
	}
	
	default JWKSet buildWith(Resource resource) {
		try {
			return JWKSet.load(resource.getInputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	default JWKSet buildWith(RSAKey key) {
		return new JWKSet(key);
	}

	
	
	static final class KeyGeneratorUtils {
		
		private KeyGeneratorUtils() {
		}
	
		static RSAKey generateRsa() {
			KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			// @formatter:off
			return new RSAKey.Builder(publicKey)
					.privateKey(privateKey)
					.keyID(UUID.randomUUID().toString())
					.algorithm(JWSAlgorithm.RS256)
					.build();
			// @formatter:on
		}

		static ECKey generateEc() {
			KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
			ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
			ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
			Curve curve = Curve.forECParameterSpec(publicKey.getParams());
			// @formatter:off
			return new ECKey.Builder(curve, publicKey)
					.privateKey(privateKey)
					.keyID(UUID.randomUUID().toString())
					.build();
			// @formatter:on
		}

		static OctetSequenceKey generateSecret() {
			SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
			// @formatter:off
			return new OctetSequenceKey.Builder(secretKey)
					.keyID(UUID.randomUUID().toString())
					.build();
			// @formatter:on
		}
		
		static SecretKey generateSecretKey() {
			SecretKey hmacKey;
			try {
				hmacKey = KeyGenerator.getInstance("HmacSha256").generateKey();
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
			return hmacKey;
		}
	
		static KeyPair generateRsaKey() {
			KeyPair keyPair;
			try {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
				keyPairGenerator.initialize(2048);
				keyPair = keyPairGenerator.generateKeyPair();
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
			return keyPair;
		}
	
		static KeyPair generateEcKey() {
			EllipticCurve ellipticCurve = new EllipticCurve(
					new ECFieldFp(
					new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951")),
					new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853948"),
					new BigInteger("41058363725152142129326129780047268409114441015993725554835256314039467401291"));
			ECPoint ecPoint = new ECPoint(
					new BigInteger("48439561293906451759052585252797914202762949526041747995844080717082404635286"),
					new BigInteger("36134250956749795798585127919587881956611106672985015071877198253568414405109"));
			ECParameterSpec ecParameterSpec = new ECParameterSpec(
					ellipticCurve,
					ecPoint,
					new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
					1);
	
			KeyPair keyPair;
			try {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
				keyPairGenerator.initialize(ecParameterSpec);
				keyPair = keyPairGenerator.generateKeyPair();
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
			return keyPair;
		}
	}		
	
}
