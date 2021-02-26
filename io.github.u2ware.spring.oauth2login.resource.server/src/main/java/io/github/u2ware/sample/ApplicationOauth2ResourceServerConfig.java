package io.github.u2ware.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class ApplicationOauth2ResourceServerConfig extends WebSecurityConfigurerAdapter {
	
	
    private @Autowired Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeRequests(authorize -> authorize
		    .anyRequest().authenticated()
		    
		).oauth2ResourceServer(resource->resource
				
			.jwt(jwt->jwt
					.jwtAuthenticationConverter(jwtAuthenticationConverter))
		);
    }


    
//    JwtAuthenticationConverter jwtAuthenticationConverter1() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }
// 
//    JwtAuthenticationConverter jwtAuthenticationConverter2() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }
//    
//    JwtAuthenticationConverter jwtAuthenticationConverter3() {
//    	CustomJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new CustomJwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//    	
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }
//    
//    JwtAuthenticationConverter jwtAuthenticationConverter4() {
//    	JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//    	
//        CustomJwtAuthenticationConverter jwtAuthenticationConverter = new CustomJwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return jwtAuthenticationConverter;
//    }
}
