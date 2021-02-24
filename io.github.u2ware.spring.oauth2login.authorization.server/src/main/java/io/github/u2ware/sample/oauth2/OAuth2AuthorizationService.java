package io.github.u2ware.sample.oauth2;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWKSet;

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
	
}
