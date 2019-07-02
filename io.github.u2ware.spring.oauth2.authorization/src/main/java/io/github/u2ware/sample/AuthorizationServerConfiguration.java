package io.github.u2ware.sample;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	
	public final static String CLIENT_ID = "hello";
	public final static String CLIENT_SECRET = "secret";
	public final static String[] SCOPES = new String[] {"READ", "WRITE"};
	//public final static String[] GRANT_TYPES = new String[] {"password", "authorization_code", "refresh_token","client_credentials"};
	public final static String[] GRANT_TYPES = new String[] {"password", "authorization_code"};
	public final static String[] REDIRECT_URIS = new String[] {"http://localhost:9091/login/oauth2/code/hello"};
	
	protected Log logger = LogFactory.getLog(getClass());

	private AuthenticationManager authenticationManager;
	private KeyPair keyPair;

	public AuthorizationServerConfiguration(AuthenticationConfiguration authenticationConfiguration, KeyPair keyPair) throws Exception {
		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.keyPair = keyPair;
	}
	
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
        	.allowFormAuthenticationForClients() // BASIC vs FORM
        	.tokenKeyAccess("permitAll()")
        	.checkTokenAccess("isAuthenticated()");
    }
	

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		//clients.
		//clients.jdbc(dataSource)
		// @formatter:off
		clients.inMemory()
			.withClient(CLIENT_ID)
				.authorizedGrantTypes(GRANT_TYPES)
				.secret("{noop}secret")
				.scopes("message:read")
                .accessTokenValiditySeconds(600_000_000)
                .redirectUris(REDIRECT_URIS)
				.and()
		;
		// @formatter:on
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// @formatter:off
		endpoints
            .authenticationManager(this.authenticationManager)
			.accessTokenConverter(accessTokenConverter())
            .tokenStore(tokenStore())
        ;
		// @formatter:on
    }
    

    
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	
	
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
		
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);
		converter.setAccessTokenConverter(accessTokenConverter);

		return converter;
	}
	
	private static class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {
		@Override
		public Map<String, ?> convertUserAuthentication(Authentication authentication) {
			Map<String, Object> response = new LinkedHashMap<String, Object>();
			response.put("sub", authentication.getName());
			response.put("aaa", "gggg");
			if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
				response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
			}
			return response;
		}
	}

	@Configuration
	public static class KeyPairConfig {

		@Bean
		public KeyPair keyPair() {
			try {
				String privateExponent = "3851612021791312596791631935569878540203393691253311342052463788814433805390794604753109719790052408607029530149004451377846406736413270923596916756321977922303381344613407820854322190592787335193581632323728135479679928871596911841005827348430783250026013354350760878678723915119966019947072651782000702927096735228356171563532131162414366310012554312756036441054404004920678199077822575051043273088621405687950081861819700809912238863867947415641838115425624808671834312114785499017269379478439158796130804789241476050832773822038351367878951389438751088021113551495469440016698505614123035099067172660197922333993";
				String modulus = "18044398961479537755088511127417480155072543594514852056908450877656126120801808993616738273349107491806340290040410660515399239279742407357192875363433659810851147557504389760192273458065587503508596714389889971758652047927503525007076910925306186421971180013159326306810174367375596043267660331677530921991343349336096643043840224352451615452251387611820750171352353189973315443889352557807329336576421211370350554195530374360110583327093711721857129170040527236951522127488980970085401773781530555922385755722534685479501240842392531455355164896023070459024737908929308707435474197069199421373363801477026083786683";
				String exponent = "65537";

				RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
				RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
				KeyFactory factory = KeyFactory.getInstance("RSA");
				return new KeyPair(factory.generatePublic(publicSpec), factory.generatePrivate(privateSpec));
			} catch ( Exception e ) {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	
	
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		// OAuth2 Client 정보 설정 
//
//		// @formatter:off
//		clients.inMemory()
//			.withClient(CLIENT_ID)
//				.authorizedGrantTypes(GRANT_TYPES)
//				.secret("{noop}"+CLIENT_SECRET)
//				.scopes(SCOPES)
//                .accessTokenValiditySeconds(600_000_000)
//                .redirectUris(REDIRECT_URIS)
//				.and();
//		// @formatter:on
//	}
//	
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		// AuthorizationServer 의 (자체) 보안 설정 , /oauth/token...
//		
//		// @formatter:off
//        security.allowFormAuthenticationForClients()
//        		.tokenKeyAccess("permitAll()")
//        		.checkTokenAccess("permitAll()");
//		// @formatter:on
//	}
//
//
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		// AuthorizationServer 의 (작동을 위한) End-point 설정, token
//		
//		// @formatter:off
//		endpoints
//	        .authenticationManager(this.authenticationManager)
//	        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//		// @formatter:on
//	}	
}


/**
 * Legacy Authorization Server (spring-security-oauth2) does not support any
 * <a href target="_blank" href="https://tools.ietf.org/html/rfc7517#section-5">JWK Set</a> endpoint.
 *
 * This class adds ad-hoc support in order to better support the other samples in the repo.
 */
//@FrameworkEndpoint
//class JwkSetEndpoint {
//	KeyPair keyPair;
//
//	public JwkSetEndpoint(KeyPair keyPair) {
//		this.keyPair = keyPair;
//	}
//
//	@GetMapping("/.well-known/jwks.json")
//	@ResponseBody
//	public Map<String, Object> getKey(Principal principal) {
//		RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
//		RSAKey key = new RSAKey.Builder(publicKey).build();
//		return new JWKSet(key).toJSONObject();
//	}
//}

/**
 * An Authorization Server will more typically have a key rotation strategy, and the keys will not
 * be hard-coded into the application code.
 *
 * For simplicity, though, this sample doesn't demonstrate key rotation.
 */
//@Configuration
//class KeyConfig {
//	@Bean
//	KeyPair keyPair() {
//		try {
//			String privateExponent = "3851612021791312596791631935569878540203393691253311342052463788814433805390794604753109719790052408607029530149004451377846406736413270923596916756321977922303381344613407820854322190592787335193581632323728135479679928871596911841005827348430783250026013354350760878678723915119966019947072651782000702927096735228356171563532131162414366310012554312756036441054404004920678199077822575051043273088621405687950081861819700809912238863867947415641838115425624808671834312114785499017269379478439158796130804789241476050832773822038351367878951389438751088021113551495469440016698505614123035099067172660197922333993";
//			String modulus = "18044398961479537755088511127417480155072543594514852056908450877656126120801808993616738273349107491806340290040410660515399239279742407357192875363433659810851147557504389760192273458065587503508596714389889971758652047927503525007076910925306186421971180013159326306810174367375596043267660331677530921991343349336096643043840224352451615452251387611820750171352353189973315443889352557807329336576421211370350554195530374360110583327093711721857129170040527236951522127488980970085401773781530555922385755722534685479501240842392531455355164896023070459024737908929308707435474197069199421373363801477026083786683";
//			String exponent = "65537";
//
//			RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent));
//			RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
//			KeyFactory factory = KeyFactory.getInstance("RSA");
//			return new KeyPair(factory.generatePublic(publicSpec), factory.generatePrivate(privateSpec));
//		} catch ( Exception e ) {
//			throw new IllegalArgumentException(e);
//		}
//	}
//}

/**
 * Legacy Authorization Server does not support a custom name for the user parameter, so we'll need
 * to extend the default. By default, it uses the attribute {@code user_name}, though it would be
 * better to adhere to the {@code sub} property defined in the
 * <a target="_blank" href="https://tools.ietf.org/html/rfc7519">JWT Specification</a>.
 */
//class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {
//	@Override
//	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
//		Map<String, Object> response = new LinkedHashMap<String, Object>();
//		response.put("sub", authentication.getName());
//		//response.put("response", authentication);
//		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
//			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
//		}
//		return response;
//	}
//}
